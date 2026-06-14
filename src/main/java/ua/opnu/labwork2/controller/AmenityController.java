package ua.opnu.labwork2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.opnu.labwork2.dto.amenity.AmenityCreateRequest;
import ua.opnu.labwork2.dto.amenity.AmenityResponse;
import ua.opnu.labwork2.dto.amenity.AmenityUpdateRequest;
import ua.opnu.labwork2.dto.common.ApiErrorResponse;
import ua.opnu.labwork2.service.AmenityService;
import ua.opnu.labwork2.service.RoomService;

import java.util.List;

@RestController
@Tag(name = "Зручності",
        description = "Управління каталогом зручностей (Wi-Fi, TV, кондиціонер тощо) та їх прив'язка до номерів. " +
                "Зв'язок між номером і зручністю - many-to-many")
public class AmenityController {

    private final AmenityService amenityService;
    private final RoomService roomService;

    public AmenityController(AmenityService amenityService, RoomService roomService) {
        this.amenityService = amenityService;
        this.roomService = roomService;
    }

    @PostMapping("/amenities")
    @Operation(summary = "Створити зручність",
            description = "Створює нову зручність у каталозі. Перевіряє: name (2–150 символів), " +
                    "description (10–2000 символів). Обидва поля обов'язкові.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Зручність створено",
                    content = @Content(schema = @Schema(implementation = AmenityResponse.class))),
            @ApiResponse(responseCode = "400", description = "Помилка валідації вхідних даних",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<AmenityResponse> createAmenity(@Valid @RequestBody AmenityCreateRequest request) {
        return ResponseEntity.ok(amenityService.create(request));
    }

    @GetMapping("/amenities")
    @Operation(summary = "Отримати всі зручності",
            description = "Повертає каталог усіх зручностей у системі.")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Список зручностей",
            content = @Content(schema = @Schema(implementation = AmenityResponse.class))))
    public ResponseEntity<List<AmenityResponse>> getAllAmenities() {
        return ResponseEntity.ok(amenityService.findAll());
    }

    @GetMapping("/amenities/{id}")
    @Operation(summary = "Отримати зручність за id",
            description = "Повертає інформацію про конкретну зручність.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Зручність знайдено",
                    content = @Content(schema = @Schema(implementation = AmenityResponse.class))),
            @ApiResponse(responseCode = "404", description = "Зручність з вказаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<AmenityResponse> getAmenityById(@PathVariable Long id) {
        return ResponseEntity.ok(amenityService.findById(id));
    }

    @PutMapping("/amenities/{id}")
    @Operation(summary = "Оновити зручність",
            description = "Оновлює name та description зручності. Правила валідації аналогічні створенню.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Зручність оновлено",
                    content = @Content(schema = @Schema(implementation = AmenityResponse.class))),
            @ApiResponse(responseCode = "400", description = "Помилка валідації вхідних даних",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Зручність з вказаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<AmenityResponse> updateAmenity(@PathVariable Long id,
                                                         @Valid @RequestBody AmenityUpdateRequest request) {
        return ResponseEntity.ok(amenityService.update(id, request));
    }

    @DeleteMapping("/amenities/{id}")
    @Operation(summary = "Видалити зручність",
            description = "Видаляє зручність з каталогу. Бізнес-правило: видалення неможливе, " +
                    "якщо зручність прив'язана хоча б до одного номера.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Зручність успішно видалено"),
            @ApiResponse(responseCode = "404", description = "Зручність з вказаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Зручність неможливо видалити, бо вона прив'язана до номерів",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<Void> deleteAmenity(@PathVariable Long id) {
        amenityService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/rooms/{id}/amenities/{amenityId}")
    @Operation(summary = "Прив'язати зручність до номера",
            description = "Додає вказану зручність до номера (зв'язок N↔M). Якщо зручність уже прив'язана, " +
                    "запит ідемпотентний і не змінює стан.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поточний список зручностей номера",
                    content = @Content(schema = @Schema(implementation = AmenityResponse.class))),
            @ApiResponse(responseCode = "404", description = "Номер або зручність не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<List<AmenityResponse>> addAmenityToRoom(@PathVariable Long id,
                                                                  @PathVariable Long amenityId) {
        return ResponseEntity.ok(roomService.addAmenity(id, amenityId));
    }

    @DeleteMapping("/rooms/{id}/amenities/{amenityId}")
    @Operation(summary = "Відв'язати зручність від номера",
            description = "Видаляє прив'язку зручності до номера. Сама зручність залишається в каталозі.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поточний список зручностей номера після видалення",
                    content = @Content(schema = @Schema(implementation = AmenityResponse.class))),
            @ApiResponse(responseCode = "404", description = "Номер або зручність не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<List<AmenityResponse>> removeAmenityFromRoom(@PathVariable Long id,
                                                                       @PathVariable Long amenityId) {
        return ResponseEntity.ok(roomService.removeAmenity(id, amenityId));
    }
}
