package ua.opnu.labwork2.mapper;

import org.springframework.stereotype.Component;
import ua.opnu.labwork2.dto.hotel.HotelCreateRequest;
import ua.opnu.labwork2.dto.hotel.HotelResponse;
import ua.opnu.labwork2.dto.hotel.HotelUpdateRequest;
import ua.opnu.labwork2.entity.Hotel;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HotelMapper {

    public Hotel toEntity(HotelCreateRequest dto) {
        Hotel h = new Hotel();
        h.setName(dto.name());
        h.setCity(dto.city());
        h.setAddress(dto.address());
        h.setRating(dto.rating());
        return h;
    }

    public void updateEntity(Hotel h, HotelUpdateRequest dto) {
        h.setName(dto.name());
        h.setCity(dto.city());
        h.setAddress(dto.address());
        h.setRating(dto.rating());
    }

    public HotelResponse toResponse(Hotel h) {
        return new HotelResponse(h.getId(), h.getName(), h.getCity(), h.getAddress(), h.getRating());
    }

    public List<HotelResponse> toResponseList(List<Hotel> list) {
        return list.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
