package ua.opnu.labwork2.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    // Багато бронювань належать одному гостю (N -> 1)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "guest_id")
    private Guest guest;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    private Room room;

    public Booking() {
    }

    public Booking(Long id, LocalDate checkInDate, LocalDate checkOutDate, BookingStatus status) {
        this.id = id;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
