package ua.opnu.labwork2.service;

import org.springframework.stereotype.Service;
import ua.opnu.labwork2.dto.guest.GuestResponse;
import ua.opnu.labwork2.dto.hotel.HotelResponse;
import ua.opnu.labwork2.dto.room.RoomResponse;
import ua.opnu.labwork2.mapper.GuestMapper;
import ua.opnu.labwork2.mapper.HotelMapper;
import ua.opnu.labwork2.mapper.RoomMapper;
import ua.opnu.labwork2.repository.GuestRepository;
import ua.opnu.labwork2.repository.HotelRepository;
import ua.opnu.labwork2.repository.RoomRepository;

import java.util.List;

@Service
public class SearchService {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;
    private final HotelMapper hotelMapper;
    private final RoomMapper roomMapper;
    private final GuestMapper guestMapper;

    public SearchService(HotelRepository hotelRepository, RoomRepository roomRepository,
                         GuestRepository guestRepository, HotelMapper hotelMapper,
                         RoomMapper roomMapper, GuestMapper guestMapper) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.guestRepository = guestRepository;
        this.hotelMapper = hotelMapper;
        this.roomMapper = roomMapper;
        this.guestMapper = guestMapper;
    }

    public List<HotelResponse> searchHotels(String query) {
        return hotelMapper.toResponseList(
                hotelRepository.findByNameContainingIgnoreCaseOrCityContainingIgnoreCase(query, query));
    }

    public List<RoomResponse> searchRooms(String query) {
        return roomMapper.toResponseList(
                roomRepository.findByTypeContainingIgnoreCaseOrNumberContainingIgnoreCase(query, query));
    }

    public List<GuestResponse> searchGuests(String query) {
        return guestMapper.toResponseList(
                guestRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query, query));
    }
}
