package ua.opnu.labwork2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.opnu.labwork2.entity.Hotel;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findByNameContainingIgnoreCaseOrCityContainingIgnoreCase(String name, String city);
}
