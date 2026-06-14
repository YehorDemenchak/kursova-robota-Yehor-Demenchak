package ua.opnu.labwork2.service;

import org.springframework.stereotype.Service;
import ua.opnu.labwork2.entity.BookingStatus;
import ua.opnu.labwork2.repository.BookingRepository;
import ua.opnu.labwork2.repository.HotelRepository;
import ua.opnu.labwork2.repository.RoomRepository;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class AnalyticsService {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    public AnalyticsService(HotelRepository hotelRepository,
                            RoomRepository roomRepository,
                            BookingRepository bookingRepository) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
    }

    public long countHotels() { return hotelRepository.count(); }
    public long countRooms() { return roomRepository.count(); }
    public long countActiveBookings() { return bookingRepository.countByStatus(BookingStatus.ACTIVE); }
    public long countCompletedBookings() { return bookingRepository.countByStatus(BookingStatus.COMPLETED); }

    public Map<String, Long> roomsByType() {
        Map<String, Long> result = new LinkedHashMap<>();
        for (Object[] row : roomRepository.countRoomsByType()) {
            result.put((String) row[0], (Long) row[1]);
        }
        return result;
    }
}
