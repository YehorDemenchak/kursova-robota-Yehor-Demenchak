package ua.opnu.labwork2.mapper;

import org.springframework.stereotype.Component;
import ua.opnu.labwork2.dto.amenity.AmenityCreateRequest;
import ua.opnu.labwork2.dto.amenity.AmenityResponse;
import ua.opnu.labwork2.dto.amenity.AmenityUpdateRequest;
import ua.opnu.labwork2.entity.Amenity;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AmenityMapper {

    public Amenity toEntity(AmenityCreateRequest dto) {
        Amenity a = new Amenity();
        a.setName(dto.name());
        a.setDescription(dto.description());
        return a;
    }

    public void updateEntity(Amenity a, AmenityUpdateRequest dto) {
        a.setName(dto.name());
        a.setDescription(dto.description());
    }

    public AmenityResponse toResponse(Amenity a) {
        return new AmenityResponse(a.getId(), a.getName(), a.getDescription());
    }

    public List<AmenityResponse> toResponseList(List<Amenity> list) {
        return list.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
