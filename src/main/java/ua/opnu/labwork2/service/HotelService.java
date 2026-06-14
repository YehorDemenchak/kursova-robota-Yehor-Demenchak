package ua.opnu.labwork2.service;

import org.springframework.stereotype.Service;
import ua.opnu.labwork2.dto.hotel.HotelCreateRequest;
import ua.opnu.labwork2.dto.hotel.HotelResponse;
import ua.opnu.labwork2.dto.hotel.HotelUpdateRequest;
import ua.opnu.labwork2.dto.room.RoomResponse;
import ua.opnu.labwork2.entity.Hotel;
import ua.opnu.labwork2.exception.ConflictOperationException;
import ua.opnu.labwork2.exception.ResourceNotFoundException;
import ua.opnu.labwork2.mapper.HotelMapper;
import ua.opnu.labwork2.mapper.RoomMapper;
import ua.opnu.labwork2.repository.HotelRepository;
import ua.opnu.labwork2.repository.RoomRepository;

import java.util.List;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final HotelMapper hotelMapper;
    private final RoomMapper roomMapper;

    public HotelService(HotelRepository hotelRepository, RoomRepository roomRepository,
                        HotelMapper hotelMapper, RoomMapper roomMapper) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.hotelMapper = hotelMapper;
        this.roomMapper = roomMapper;
    }

    public HotelResponse create(HotelCreateRequest dto) {
        Hotel saved = hotelRepository.save(hotelMapper.toEntity(dto));
        return hotelMapper.toResponse(saved);
    }

    public List<HotelResponse> findAll() {
        return hotelMapper.toResponseList(hotelRepository.findAll());
    }

    public HotelResponse findById(Long id) {
        return hotelMapper.toResponse(getOrThrow(id));
    }

    public HotelResponse update(Long id, HotelUpdateRequest dto) {
        Hotel hotel = getOrThrow(id);
        hotelMapper.updateEntity(hotel, dto);
        return hotelMapper.toResponse(hotelRepository.save(hotel));
    }

    /**
     * Бізнес-правило 4: не можна видалити готель, якщо в ньому є номери.
     */
    public void delete(Long id) {
        Hotel hotel = getOrThrow(id);
        if (!roomRepository.findByHotelId(id).isEmpty()) {
            throw new ConflictOperationException(
                    "Не можна видалити готель з id=" + id + ": існують пов'язані номери");
        }
        hotelRepository.delete(hotel);
    }

    // Запит, що стосується кількох сутностей
    public List<RoomResponse> getRooms(Long hotelId) {
        getOrThrow(hotelId);
        return roomMapper.toResponseList(roomRepository.findByHotelId(hotelId));
    }

    private Hotel getOrThrow(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Готель", id));
    }
}
