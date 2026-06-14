package ua.opnu.labwork2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.opnu.labwork2.entity.Booking;
import ua.opnu.labwork2.entity.BookingStatus;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByGuestId(Long guestId);
    List<Booking> findByRoomId(Long roomId);
    List<Booking> findByStatus(BookingStatus status);
    long countByStatus(BookingStatus status);

    // Для перевірки активних бронювань
    boolean existsByGuestIdAndStatusIn(Long guestId, Collection<BookingStatus> statuses);
    boolean existsByRoomIdAndStatusIn(Long roomId, Collection<BookingStatus> statuses);

    // Пошук перетинів дат для одного номера
    @Query("""
            SELECT b FROM Booking b
            WHERE b.room.id = :roomId
              AND b.status IN :activeStatuses
              AND (:excludeBookingId IS NULL OR b.id <> :excludeBookingId)
              AND b.checkInDate < :newCheckOut
              AND (b.checkOutDate IS NULL OR b.checkOutDate > :newCheckIn)
            """)
    List<Booking> findOverlapping(@Param("roomId") Long roomId,
                                  @Param("newCheckIn") LocalDate newCheckIn,
                                  @Param("newCheckOut") LocalDate newCheckOut,
                                  @Param("activeStatuses") Collection<BookingStatus> activeStatuses,
                                  @Param("excludeBookingId") Long excludeBookingId);
}
