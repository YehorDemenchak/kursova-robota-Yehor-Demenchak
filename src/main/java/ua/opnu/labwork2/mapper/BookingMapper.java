package ua.opnu.labwork2.mapper;

import org.springframework.stereotype.Component;
import ua.opnu.labwork2.dto.booking.BookingCreateRequest;
import ua.opnu.labwork2.dto.booking.BookingResponse;
import ua.opnu.labwork2.dto.booking.BookingUpdateRequest;
import ua.opnu.labwork2.entity.Booking;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookingMapper {

    public Booking toEntity(BookingCreateRequest dto) {
        Booking b = new Booking();
        b.setCheckInDate(dto.checkInDate());
        b.setCheckOutDate(dto.checkOutDate());
        b.setStatus(dto.status());
        return b;
    }

    public void updateEntity(Booking b, BookingUpdateRequest dto) {
        b.setCheckInDate(dto.checkInDate());
        b.setCheckOutDate(dto.checkOutDate());
        b.setStatus(dto.status());
    }

    public BookingResponse toResponse(Booking b) {
        return new BookingResponse(b.getId(), b.getCheckInDate(), b.getCheckOutDate(), b.getStatus());
    }

    public List<BookingResponse> toResponseList(List<Booking> list) {
        return list.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
