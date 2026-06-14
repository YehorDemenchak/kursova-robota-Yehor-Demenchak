package ua.opnu.labwork2.mapper;

import org.springframework.stereotype.Component;
import ua.opnu.labwork2.dto.room.RoomCreateRequest;
import ua.opnu.labwork2.dto.room.RoomResponse;
import ua.opnu.labwork2.dto.room.RoomUpdateRequest;
import ua.opnu.labwork2.entity.Room;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomMapper {

    public Room toEntity(RoomCreateRequest dto) {
        Room r = new Room();
        r.setNumber(dto.number());
        r.setType(dto.type());
        r.setPricePerNight(dto.pricePerNight());
        r.setCapacity(dto.capacity());
        return r;
    }

    public void updateEntity(Room r, RoomUpdateRequest dto) {
        r.setNumber(dto.number());
        r.setType(dto.type());
        r.setPricePerNight(dto.pricePerNight());
        r.setCapacity(dto.capacity());
    }

    public RoomResponse toResponse(Room r) {
        return new RoomResponse(r.getId(), r.getNumber(), r.getType(), r.getPricePerNight(), r.getCapacity());
    }

    public List<RoomResponse> toResponseList(List<Room> list) {
        return list.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
