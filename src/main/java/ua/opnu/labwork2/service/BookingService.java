package ua.opnu.labwork2.service;

import org.springframework.stereotype.Service;
import ua.opnu.labwork2.dto.booking.BookingCreateRequest;
import ua.opnu.labwork2.dto.booking.BookingResponse;
import ua.opnu.labwork2.dto.booking.BookingUpdateRequest;
import ua.opnu.labwork2.entity.Booking;
import ua.opnu.labwork2.entity.BookingStatus;
import ua.opnu.labwork2.entity.Guest;
import ua.opnu.labwork2.entity.Room;
import ua.opnu.labwork2.exception.BadRequestException;
import ua.opnu.labwork2.exception.ConflictOperationException;
import ua.opnu.labwork2.exception.ResourceNotFoundException;
import ua.opnu.labwork2.mapper.BookingMapper;
import ua.opnu.labwork2.repository.BookingRepository;
import ua.opnu.labwork2.repository.GuestRepository;
import ua.opnu.labwork2.repository.RoomRepository;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;

@Service
public class BookingService {

    private static final EnumSet<BookingStatus> ACTIVE_LIKE =
            EnumSet.of(BookingStatus.PENDING, BookingStatus.CONFIRMED, BookingStatus.ACTIVE);

    private final BookingRepository bookingRepository;
    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;
    private final BookingMapper bookingMapper;

    public BookingService(BookingRepository bookingRepository,
                          GuestRepository guestRepository,
                          RoomRepository roomRepository,
                          BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
        this.bookingMapper = bookingMapper;
    }

    /**
     * Бізнес-правила:
     *  2. Пов'язані сутності (Guest, Room) мають існувати.
     *  3. Для одного номера не можна створити два активних бронювання з перетином дат.
     *  5. checkOutDate (якщо вказано) має бути не раніше checkInDate.
     */
    public BookingResponse create(BookingCreateRequest dto, Long guestId, Long roomId) {
        validateDates(dto.checkInDate(), dto.checkOutDate());

        Booking booking = bookingMapper.toEntity(dto);

        if (guestId != null) {
            Guest guest = guestRepository.findById(guestId)
                    .orElseThrow(() -> new ResourceNotFoundException("Гість", guestId));
            booking.setGuest(guest);
        }
        if (roomId != null) {
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new ResourceNotFoundException("Номер", roomId));
            booking.setRoom(room);
        }

        if (roomId != null && ACTIVE_LIKE.contains(dto.status())) {
            ensureNoOverlap(roomId, dto.checkInDate(), dto.checkOutDate(), null);
        }

        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    public List<BookingResponse> findAll() {
        return bookingMapper.toResponseList(bookingRepository.findAll());
    }

    public BookingResponse findById(Long id) {
        return bookingMapper.toResponse(getOrThrow(id));
    }

    public BookingResponse update(Long id, BookingUpdateRequest dto, Long guestId, Long roomId) {
        validateDates(dto.checkInDate(), dto.checkOutDate());

        Booking booking = getOrThrow(id);
        bookingMapper.updateEntity(booking, dto);

        if (guestId != null) {
            Guest guest = guestRepository.findById(guestId)
                    .orElseThrow(() -> new ResourceNotFoundException("Гість", guestId));
            booking.setGuest(guest);
        }
        if (roomId != null) {
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new ResourceNotFoundException("Номер", roomId));
            booking.setRoom(room);
        }

        Long effectiveRoomId = roomId != null ? roomId :
                (booking.getRoom() != null ? booking.getRoom().getId() : null);
        if (effectiveRoomId != null && ACTIVE_LIKE.contains(dto.status())) {
            ensureNoOverlap(effectiveRoomId, dto.checkInDate(), dto.checkOutDate(), id);
        }

        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    public void delete(Long id) {
        Booking booking = getOrThrow(id);
        bookingRepository.delete(booking);
    }

    public List<BookingResponse> findActive() {
        return bookingMapper.toResponseList(bookingRepository.findByStatus(BookingStatus.ACTIVE));
    }

    // ---------- private helpers ----------

    private Booking getOrThrow(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Бронювання", id));
    }

    private void validateDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkOut != null && checkOut.isBefore(checkIn)) {
            throw new BadRequestException(
                    "checkOutDate (" + checkOut + ") не може бути раніше checkInDate (" + checkIn + ")");
        }
    }

    /**
     * Перевіряє, що для номера немає активних бронювань з перетином дат.
     * Якщо excludeBookingId != null, цей запис ігнорується (для update).
     */
    private void ensureNoOverlap(Long roomId, LocalDate checkIn, LocalDate checkOut,
                                 Long excludeBookingId) {
        LocalDate effectiveCheckOut = checkOut != null ? checkOut : checkIn.plusDays(1);
        List<Booking> overlaps = bookingRepository.findOverlapping(
                roomId, checkIn, effectiveCheckOut, ACTIVE_LIKE, excludeBookingId);
        if (!overlaps.isEmpty()) {
            throw new ConflictOperationException(
                    "Номер з id=" + roomId + " вже має активне бронювання з перетином дат");
        }
    }
}
