package ua.opnu.labwork2.mapper;

import org.springframework.stereotype.Component;
import ua.opnu.labwork2.dto.guest.GuestCreateRequest;
import ua.opnu.labwork2.dto.guest.GuestResponse;
import ua.opnu.labwork2.dto.guest.GuestUpdateRequest;
import ua.opnu.labwork2.entity.Guest;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GuestMapper {

    public Guest toEntity(GuestCreateRequest dto) {
        Guest g = new Guest();
        g.setFirstName(dto.firstName());
        g.setLastName(dto.lastName());
        g.setEmail(dto.email());
        g.setPhone(dto.phone());
        return g;
    }

    public void updateEntity(Guest g, GuestUpdateRequest dto) {
        g.setFirstName(dto.firstName());
        g.setLastName(dto.lastName());
        g.setEmail(dto.email());
        g.setPhone(dto.phone());
    }

    public GuestResponse toResponse(Guest g) {
        return new GuestResponse(g.getId(), g.getFirstName(), g.getLastName(), g.getEmail(), g.getPhone());
    }

    public List<GuestResponse> toResponseList(List<Guest> list) {
        return list.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
