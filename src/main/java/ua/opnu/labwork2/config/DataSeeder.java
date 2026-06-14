package ua.opnu.labwork2.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ua.opnu.labwork2.entity.*;
import ua.opnu.labwork2.repository.*;

import java.time.LocalDate;
import java.util.List;


@Component
public class DataSeeder implements CommandLineRunner {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;
    private final AmenityRepository amenityRepository;
    private final BookingRepository bookingRepository;

    public DataSeeder(HotelRepository hotelRepository,
                      RoomRepository roomRepository,
                      GuestRepository guestRepository,
                      AmenityRepository amenityRepository,
                      BookingRepository bookingRepository) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.guestRepository = guestRepository;
        this.amenityRepository = amenityRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public void run(String... args) {
        if (hotelRepository.count() > 0) {
            return;
        }

        Hotel hilton = hotelRepository.save(new Hotel(null, "Hilton", "Kyiv", "Khreshchatyk 1", 5));
        Hotel premier = hotelRepository.save(new Hotel(null, "Premier", "Lviv", "Svobody 12", 4));

        Amenity wifi = amenityRepository.save(new Amenity(null, "Wi-Fi", "Безкоштовний бездротовий інтернет"));
        Amenity tv = amenityRepository.save(new Amenity(null, "TV", "Телевізор з кабельними каналами"));
        Amenity ac = amenityRepository.save(new Amenity(null, "Кондиціонер", "Клімат-контроль"));

        Room r101 = new Room(null, "101", "Standard", 100.0, 2);
        r101.setHotel(hilton);
        r101.getAmenities().add(wifi);
        r101.getAmenities().add(tv);
        r101 = roomRepository.save(r101);

        Room r102 = new Room(null, "102", "Deluxe", 180.0, 3);
        r102.setHotel(hilton);
        r102.getAmenities().add(wifi);
        r102.getAmenities().add(ac);
        r102 = roomRepository.save(r102);

        Room r201 = new Room(null, "201", "Standard", 90.0, 2);
        r201.setHotel(premier);
        r201.getAmenities().add(wifi);
        r201 = roomRepository.save(r201);

        Guest ivan = guestRepository.save(new Guest(null, "Ivan", "Koval", "ivan.koval@example.com", "+380501234567"));
        Guest olena = guestRepository.save(new Guest(null, "Olena", "Shevchenko", "olena.s@example.com", "+380671112233"));

        Booking b1 = new Booking(null, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 5), BookingStatus.CONFIRMED);
        b1.setGuest(ivan);
        b1.setRoom(r101);
        bookingRepository.save(b1);

        Booking b2 = new Booking(null, LocalDate.of(2026, 4, 25), LocalDate.of(2026, 6, 10), BookingStatus.ACTIVE);
        b2.setGuest(ivan);
        b2.setRoom(r102);
        bookingRepository.save(b2);

        Booking b3 = new Booking(null, LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 4), BookingStatus.COMPLETED);
        b3.setGuest(olena);
        b3.setRoom(r201);
        bookingRepository.save(b3);
    }
}
