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
import ua.opnu.labwork2.dto.amenity.AmenityResponse;
import ua.opnu.labwork2.dto.booking.BookingResponse;
import ua.opnu.labwork2.dto.common.ApiErrorResponse;
import ua.opnu.labwork2.dto.room.RoomCreateRequest;
import ua.opnu.labwork2.dto.room.RoomResponse;
import ua.opnu.labwork2.dto.room.RoomUpdateRequest;
import ua.opnu.labwork2.service.RoomService;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@Tag(name = "Номери",
        description = "Управління номерами готелів: створення, редагування, видалення, прив'язка до готелю, " +
                "перегляд зручностей та бронювань номера")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    @Operation(summary = "Створити номер",
            description = "Створює новий номер у вказаному готелі. Перевіряє: number (2–30 символів), " +
                    "type (одне зі значень Standard, Deluxe, Suite, Family, Presidential), " +
                    "pricePerNight (≥ 0), capacity (> 0). " +
                    "Параметр hotelId є необов'язковим: якщо вказано, готель має існувати.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Номер успішно створено",
                    content = @Content(schema = @Schema(implementation = RoomResponse.class))),
            @ApiResponse(responseCode = "400", description = "Помилка валідації вхідних даних",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Готель з вказаним hotelId не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody RoomCreateRequest request,
                                                   @RequestParam(required = false) Long hotelId) {
        return ResponseEntity.ok(roomService.create(request, hotelId));
    }

    @GetMapping
    @Operation(summary = "Отримати всі номери",
            description = "Повертає повний список номерів у системі.")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Список номерів",
            content = @Content(schema = @Schema(implementation = RoomResponse.class))))
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        return ResponseEntity.ok(roomService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Отримати номер за id",
            description = "Повертає інформацію про конкретний номер.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Номер знайдено",
                    content = @Content(schema = @Schema(implementation = RoomResponse.class))),
            @ApiResponse(responseCode = "404", description = "Номер з вказаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Оновити номер",
            description = "Оновлює всі поля номера. Правила валідації аналогічні створенню. " +
                    "Може бути змінений прив'язаний готель через параметр hotelId.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Номер оновлено",
                    content = @Content(schema = @Schema(implementation = RoomResponse.class))),
            @ApiResponse(responseCode = "400", description = "Помилка валідації вхідних даних",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Номер або готель не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long id,
                                                   @Valid @RequestBody RoomUpdateRequest request,
                                                   @RequestParam(required = false) Long hotelId) {
        return ResponseEntity.ok(roomService.update(id, request, hotelId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Видалити номер",
            description = "Видаляє номер. Бізнес-правило: видалення неможливе, якщо існують активні " +
                    "бронювання на цей номер (статуси PENDING, CONFIRMED або ACTIVE).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Номер успішно видалено"),
            @ApiResponse(responseCode = "404", description = "Номер з вказаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Номер неможливо видалити через активні бронювання",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/hotel/{hotelId}")
    @Operation(summary = "Отримати номери конкретного готелю",
            description = "Альтернативний шлях для отримання списку номерів за id готелю.")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Список номерів готелю",
            content = @Content(schema = @Schema(implementation = RoomResponse.class))))
    public ResponseEntity<List<RoomResponse>> getRoomsByHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(roomService.findByHotel(hotelId));
    }

    @GetMapping("/{id}/amenities")
    @Operation(summary = "Отримати зручності номера",
            description = "Повертає список зручностей, прив'язаних до номера (зв'язок N↔M).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список зручностей номера",
                    content = @Content(schema = @Schema(implementation = AmenityResponse.class))),
            @ApiResponse(responseCode = "404", description = "Номер з вказаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<List<AmenityResponse>> getRoomAmenities(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getAmenities(id));
    }

    @GetMapping("/{id}/bookings")
    @Operation(summary = "Отримати бронювання номера",
            description = "Повертає список усіх бронювань, пов'язаних з вказаним номером.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список бронювань номера",
                    content = @Content(schema = @Schema(implementation = BookingResponse.class))),
            @ApiResponse(responseCode = "404", description = "Номер з вказаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<List<BookingResponse>> getRoomBookings(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getBookings(id));
    }
}
