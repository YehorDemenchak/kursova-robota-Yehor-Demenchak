package ua.opnu.labwork2.service;

import org.springframework.stereotype.Service;
import ua.opnu.labwork2.dto.booking.BookingResponse;
import ua.opnu.labwork2.dto.guest.GuestCreateRequest;
import ua.opnu.labwork2.dto.guest.GuestResponse;
import ua.opnu.labwork2.dto.guest.GuestUpdateRequest;
import ua.opnu.labwork2.entity.BookingStatus;
import ua.opnu.labwork2.entity.Guest;
import ua.opnu.labwork2.exception.ConflictOperationException;
import ua.opnu.labwork2.exception.DuplicateResourceException;
import ua.opnu.labwork2.exception.ResourceNotFoundException;
import ua.opnu.labwork2.mapper.BookingMapper;
import ua.opnu.labwork2.mapper.GuestMapper;
import ua.opnu.labwork2.repository.BookingRepository;
import ua.opnu.labwork2.repository.GuestRepository;

import java.util.EnumSet;
import java.util.List;

@Service
public class GuestService {

    private static final EnumSet<BookingStatus> ACTIVE_LIKE =
            EnumSet.of(BookingStatus.PENDING, BookingStatus.CONFIRMED, BookingStatus.ACTIVE);

    private final GuestRepository guestRepository;
    private final BookingRepository bookingRepository;
    private final GuestMapper guestMapper;
    private final BookingMapper bookingMapper;

    public GuestService(GuestRepository guestRepository, BookingRepository bookingRepository,
                        GuestMapper guestMapper, BookingMapper bookingMapper) {
        this.guestRepository = guestRepository;
        this.bookingRepository = bookingRepository;
        this.guestMapper = guestMapper;
        this.bookingMapper = bookingMapper;
    }

    /**
     * Бізнес-правило 1: email має бути унікальним серед усіх гостей.
     */
    public GuestResponse create(GuestCreateRequest dto) {
        if (guestRepository.existsByEmail(dto.email())) {
            throw new DuplicateResourceException("Гість з email '" + dto.email() + "' уже існує");
        }
        Guest saved = guestRepository.save(guestMapper.toEntity(dto));
        return guestMapper.toResponse(saved);
    }

    public List<GuestResponse> findAll() {
        return guestMapper.toResponseList(guestRepository.findAll());
    }

    public GuestResponse findById(Long id) {
        return guestMapper.toResponse(getOrThrow(id));
    }

    /**
     * Бізнес-правило 1 (також для update): email не може дублювати чужий.
     */
    public GuestResponse update(Long id, GuestUpdateRequest dto) {
        Guest guest = getOrThrow(id);
        // якщо email змінюється - перевіряємо унікальність
        if (!dto.email().equalsIgnoreCase(guest.getEmail())
                && guestRepository.existsByEmail(dto.email())) {
            throw new DuplicateResourceException("Гість з email '" + dto.email() + "' уже існує");
        }
        guestMapper.updateEntity(guest, dto);
        return guestMapper.toResponse(guestRepository.save(guest));
    }

    /**
     * Бізнес-правило 4: не можна видалити гостя з активними бронюваннями.
     */
    public void delete(Long id) {
        Guest guest = getOrThrow(id);
        if (bookingRepository.existsByGuestIdAndStatusIn(id, ACTIVE_LIKE)) {
            throw new ConflictOperationException(
                    "Не можна видалити гостя з id=" + id + ": існують активні бронювання");
        }
        guestRepository.delete(guest);
    }

    public List<BookingResponse> getBookings(Long guestId) {
        getOrThrow(guestId);
        return bookingMapper.toResponseList(bookingRepository.findByGuestId(guestId));
    }

    private Guest getOrThrow(Long id) {
        return guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Гість", id));
    }
}
