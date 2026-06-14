package ua.opnu.labwork2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.opnu.labwork2.entity.Amenity;

public interface AmenityRepository extends JpaRepository<Amenity, Long> {

    @Query("SELECT COUNT(r) FROM Amenity a JOIN a.rooms r WHERE a.id = :id")
    long countLinkedRooms(@Param("id") Long id);
}
