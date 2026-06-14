package ua.opnu.labwork2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.opnu.labwork2.entity.Guest;

import java.util.List;
import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, Long> {

    // Пошук гостей за іменем, прізвищем або імейлом
    List<Guest> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String firstName, String lastName, String email);

    Optional<Guest> findByEmail(String email);
    boolean existsByEmail(String email);
}
