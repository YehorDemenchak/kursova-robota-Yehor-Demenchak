package ua.opnu.labwork2.service;

import org.springframework.stereotype.Service;
import ua.opnu.labwork2.dto.amenity.AmenityCreateRequest;
import ua.opnu.labwork2.dto.amenity.AmenityResponse;
import ua.opnu.labwork2.dto.amenity.AmenityUpdateRequest;
import ua.opnu.labwork2.entity.Amenity;
import ua.opnu.labwork2.exception.ConflictOperationException;
import ua.opnu.labwork2.exception.ResourceNotFoundException;
import ua.opnu.labwork2.mapper.AmenityMapper;
import ua.opnu.labwork2.repository.AmenityRepository;

import java.util.List;

@Service
public class AmenityService {

    private final AmenityRepository amenityRepository;
    private final AmenityMapper amenityMapper;

    public AmenityService(AmenityRepository amenityRepository, AmenityMapper amenityMapper) {
        this.amenityRepository = amenityRepository;
        this.amenityMapper = amenityMapper;
    }

    public AmenityResponse create(AmenityCreateRequest dto) {
        Amenity saved = amenityRepository.save(amenityMapper.toEntity(dto));
        return amenityMapper.toResponse(saved);
    }

    public List<AmenityResponse> findAll() {
        return amenityMapper.toResponseList(amenityRepository.findAll());
    }

    public AmenityResponse findById(Long id) {
        return amenityMapper.toResponse(getOrThrow(id));
    }

    public AmenityResponse update(Long id, AmenityUpdateRequest dto) {
        Amenity amenity = getOrThrow(id);
        amenityMapper.updateEntity(amenity, dto);
        return amenityMapper.toResponse(amenityRepository.save(amenity));
    }

    /**
     * Бізнес-правило 4: не можна видалити зручність, якщо вона прив'язана хоча б до одного номера.
     */
    public void delete(Long id) {
        Amenity amenity = getOrThrow(id);
        if (amenityRepository.countLinkedRooms(id) > 0) {
            throw new ConflictOperationException(
                    "Не можна видалити зручність з id=" + id + ": вона прив'язана до номерів");
        }
        amenityRepository.delete(amenity);
    }

    private Amenity getOrThrow(Long id) {
        return amenityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Зручність", id));
    }
}
