package ua.opnu.labwork2.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.opnu.labwork2.dto.amenity.AmenityResponse;
import ua.opnu.labwork2.dto.booking.BookingResponse;
import ua.opnu.labwork2.dto.room.RoomCreateRequest;
import ua.opnu.labwork2.dto.room.RoomResponse;
import ua.opnu.labwork2.dto.room.RoomUpdateRequest;
import ua.opnu.labwork2.entity.Amenity;
import ua.opnu.labwork2.entity.BookingStatus;
import ua.opnu.labwork2.entity.Hotel;
import ua.opnu.labwork2.entity.Room;
import ua.opnu.labwork2.exception.ConflictOperationException;
import ua.opnu.labwork2.exception.ResourceNotFoundException;
import ua.opnu.labwork2.mapper.AmenityMapper;
import ua.opnu.labwork2.mapper.BookingMapper;
import ua.opnu.labwork2.mapper.RoomMapper;
import ua.opnu.labwork2.repository.AmenityRepository;
import ua.opnu.labwork2.repository.BookingRepository;
import ua.opnu.labwork2.repository.HotelRepository;
import ua.opnu.labwork2.repository.RoomRepository;

import java.util.EnumSet;
import java.util.List;

@Service
public class RoomService {

    private static final EnumSet<BookingStatus> ACTIVE_LIKE =
            EnumSet.of(BookingStatus.PENDING, BookingStatus.CONFIRMED, BookingStatus.ACTIVE);

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final AmenityRepository amenityRepository;
    private final BookingRepository bookingRepository;
    private final RoomMapper roomMapper;
    private final AmenityMapper amenityMapper;
    private final BookingMapper bookingMapper;

    public RoomService(RoomRepository roomRepository,
                       HotelRepository hotelRepository,
                       AmenityRepository amenityRepository,
                       BookingRepository bookingRepository,
                       RoomMapper roomMapper,
                       AmenityMapper amenityMapper,
                       BookingMapper bookingMapper) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
        this.amenityRepository = amenityRepository;
        this.bookingRepository = bookingRepository;
        this.roomMapper = roomMapper;
        this.amenityMapper = amenityMapper;
        this.bookingMapper = bookingMapper;
    }

    /**
     * Бізнес-правило 2: пов'язана сутність (готель) має існувати.
     */
    public RoomResponse create(RoomCreateRequest dto, Long hotelId) {
        Room room = roomMapper.toEntity(dto);
        if (hotelId != null) {
            Hotel hotel = hotelRepository.findById(hotelId)
                    .orElseThrow(() -> new ResourceNotFoundException("Готель", hotelId));
            room.setHotel(hotel);
        }
        return roomMapper.toResponse(roomRepository.save(room));
    }

    public List<RoomResponse> findAll() {
        return roomMapper.toResponseList(roomRepository.findAll());
    }

    public RoomResponse findById(Long id) {
        return roomMapper.toResponse(getOrThrow(id));
    }

    public RoomResponse update(Long id, RoomUpdateRequest dto, Long hotelId) {
        Room room = getOrThrow(id);
        roomMapper.updateEntity(room, dto);
        if (hotelId != null) {
            Hotel hotel = hotelRepository.findById(hotelId)
                    .orElseThrow(() -> new ResourceNotFoundException("Готель", hotelId));
            room.setHotel(hotel);
        }
        return roomMapper.toResponse(roomRepository.save(room));
    }

    /**
     * Бізнес-правило 4: не можна видалити номер з активними бронюваннями.
     */
    public void delete(Long id) {
        Room room = getOrThrow(id);
        if (bookingRepository.existsByRoomIdAndStatusIn(id, ACTIVE_LIKE)) {
            throw new ConflictOperationException(
                    "Не можна видалити номер з id=" + id + ": існують активні бронювання");
        }
        roomRepository.delete(room);
    }

    public List<RoomResponse> findByHotel(Long hotelId) {
        return roomMapper.toResponseList(roomRepository.findByHotelId(hotelId));
    }

    public List<AmenityResponse> getAmenities(Long roomId) {
        getOrThrow(roomId);
        return amenityMapper.toResponseList(roomRepository.findAmenitiesByRoomId(roomId));
    }

    public List<BookingResponse> getBookings(Long roomId) {
        getOrThrow(roomId);
        return bookingMapper.toResponseList(bookingRepository.findByRoomId(roomId));
    }

    @Transactional
    public List<AmenityResponse> addAmenity(Long roomId, Long amenityId) {
        Room room = getOrThrow(roomId);
        Amenity amenity = amenityRepository.findById(amenityId)
                .orElseThrow(() -> new ResourceNotFoundException("Зручність", amenityId));
        boolean alreadyLinked = room.getAmenities().stream()
                .anyMatch(a -> a.getId().equals(amenityId));
        if (!alreadyLinked) {
            room.getAmenities().add(amenity);
            roomRepository.save(room);
        }
        return amenityMapper.toResponseList(room.getAmenities());
    }

    @Transactional
    public List<AmenityResponse> removeAmenity(Long roomId, Long amenityId) {
        Room room = getOrThrow(roomId);
        room.getAmenities().removeIf(a -> a.getId().equals(amenityId));
        roomRepository.save(room);
        return amenityMapper.toResponseList(room.getAmenities());
    }

    private Room getOrThrow(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Номер", id));
    }
}
