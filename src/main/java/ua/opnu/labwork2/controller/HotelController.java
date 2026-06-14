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
import ua.opnu.labwork2.dto.common.ApiErrorResponse;
import ua.opnu.labwork2.dto.hotel.HotelCreateRequest;
import ua.opnu.labwork2.dto.hotel.HotelResponse;
import ua.opnu.labwork2.dto.hotel.HotelUpdateRequest;
import ua.opnu.labwork2.dto.room.RoomResponse;
import ua.opnu.labwork2.service.HotelService;

import java.util.List;

@RestController
@RequestMapping("/hotels")
@Tag(name = "Готелі",
        description = "Управління готелями: створення, редагування, видалення та перегляд готелів, " +
                "а також отримання списку номерів конкретного готелю")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @PostMapping
    @Operation(
            summary = "Створити готель",
            description = "Створює новий готель у системі. Перевіряє: name (2–150 символів, не порожнє), " +
                    "city (2–150 символів), address (5–200 символів), rating (від 0 до 5). " +
                    "Усі поля є обов'язковими."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Готель успішно створено",
                    content = @Content(schema = @Schema(implementation = HotelResponse.class))),
            @ApiResponse(responseCode = "400", description = "Помилка валідації вхідних даних",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<HotelResponse> createHotel(@Valid @RequestBody HotelCreateRequest request) {
        return ResponseEntity.ok(hotelService.create(request));
    }

    @GetMapping
    @Operation(summary = "Отримати всі готелі",
            description = "Повертає повний список готелів у системі без пагінації.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список готелів",
                    content = @Content(schema = @Schema(implementation = HotelResponse.class)))
    })
    public ResponseEntity<List<HotelResponse>> getAllHotels() {
        return ResponseEntity.ok(hotelService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Отримати готель за id",
            description = "Повертає інформацію про конкретний готель за його ідентифікатором.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Готель знайдено",
                    content = @Content(schema = @Schema(implementation = HotelResponse.class))),
            @ApiResponse(responseCode = "404", description = "Готель з вказаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<HotelResponse> getHotelById(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Оновити готель",
            description = "Оновлює всі поля готелю. Правила валідації аналогічні створенню: " +
                    "name (2–150), city (2–150), address (5–200), rating (0–5).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Готель оновлено",
                    content = @Content(schema = @Schema(implementation = HotelResponse.class))),
            @ApiResponse(responseCode = "400", description = "Помилка валідації вхідних даних",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Готель з вказаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<HotelResponse> updateHotel(@PathVariable Long id,
                                                     @Valid @RequestBody HotelUpdateRequest request) {
        return ResponseEntity.ok(hotelService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Видалити готель",
            description = "Видаляє готель за його id. Бізнес-правило: видалення неможливе, " +
                    "якщо в готелі є хоча б один номер.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Готель успішно видалено"),
            @ApiResponse(responseCode = "404", description = "Готель з вказаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Готель неможливо видалити, бо існують пов'язані номери",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        hotelService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/rooms")
    @Operation(summary = "Отримати номери готелю",
            description = "Повертає список усіх номерів, що належать готелю з вказаним id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список номерів готелю",
                    content = @Content(schema = @Schema(implementation = RoomResponse.class))),
            @ApiResponse(responseCode = "404", description = "Готель з вказаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<List<RoomResponse>> getHotelRooms(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.getRooms(id));
    }
}
