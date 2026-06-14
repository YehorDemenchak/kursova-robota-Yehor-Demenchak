package ua.opnu.labwork2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.opnu.labwork2.entity.Amenity;
import ua.opnu.labwork2.entity.Room;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByHotelId(Long hotelId);

    List<Room> findByTypeContainingIgnoreCaseOrNumberContainingIgnoreCase(String type, String number);

    @Query("SELECT a FROM Room r JOIN r.amenities a WHERE r.id = :roomId")
    List<Amenity> findAmenitiesByRoomId(@Param("roomId") Long roomId);

    @Query("SELECT r.type, COUNT(r) FROM Room r GROUP BY r.type")
    List<Object[]> countRoomsByType();
}
