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
import ua.opnu.labwork2.dto.booking.BookingResponse;
import ua.opnu.labwork2.dto.common.ApiErrorResponse;
import ua.opnu.labwork2.dto.guest.GuestCreateRequest;
import ua.opnu.labwork2.dto.guest.GuestResponse;
import ua.opnu.labwork2.dto.guest.GuestUpdateRequest;
import ua.opnu.labwork2.service.GuestService;

import java.util.List;

@RestController
@RequestMapping("/guests")
@Tag(name = "Гості",
        description = "Управління гостями готелю: реєстрація, оновлення контактних даних, видалення, " +
                "перегляд бронювань гостя")
public class GuestController {

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @PostMapping
    @Operation(summary = "Зареєструвати нового гостя",
            description = "Створює нового гостя. Перевіряє: firstName (2–50 символів), lastName (2–50 символів), " +
                    "email (валідний формат, до 100 символів, унікальний у системі), " +
                    "phone (10–20 символів, лише цифри, пробіли та символ +). " +
                    "Бізнес-правило: email має бути унікальним.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Гостя зареєстровано",
                    content = @Content(schema = @Schema(implementation = GuestResponse.class))),
            @ApiResponse(responseCode = "400", description = "Помилка валідації вхідних даних",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Гість із таким email уже існує",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<GuestResponse> createGuest(@Valid @RequestBody GuestCreateRequest request) {
        return ResponseEntity.ok(guestService.create(request));
    }

    @GetMapping
    @Operation(summary = "Отримати всіх гостей",
            description = "Повертає повний список зареєстрованих гостей.")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Список гостей",
            content = @Content(schema = @Schema(implementation = GuestResponse.class))))
    public ResponseEntity<List<GuestResponse>> getAllGuests() {
        return ResponseEntity.ok(guestService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Отримати гостя за id",
            description = "Повертає інформацію про конкретного гостя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Гостя знайдено",
                    content = @Content(schema = @Schema(implementation = GuestResponse.class))),
            @ApiResponse(responseCode = "404", description = "Гостя з вказаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<GuestResponse> getGuestById(@PathVariable Long id) {
        return ResponseEntity.ok(guestService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Оновити дані гостя",
            description = "Оновлює всі контактні дані гостя. Якщо змінюється email, перевіряється його " +
                    "унікальність серед інших гостей системи.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Дані гостя оновлено",
                    content = @Content(schema = @Schema(implementation = GuestResponse.class))),
            @ApiResponse(responseCode = "400", description = "Помилка валідації вхідних даних",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Гостя з вказаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Інший гість уже використовує цей email",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<GuestResponse> updateGuest(@PathVariable Long id,
                                                     @Valid @RequestBody GuestUpdateRequest request) {
        return ResponseEntity.ok(guestService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Видалити гостя",
            description = "Видаляє гостя з системи. Бізнес-правило: видалення неможливе, якщо у гостя є " +
                    "активні бронювання (статуси PENDING, CONFIRMED або ACTIVE).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Гостя успішно видалено"),
            @ApiResponse(responseCode = "404", description = "Гостя з вказаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Гостя неможливо видалити через активні бронювання",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<Void> deleteGuest(@PathVariable Long id) {
        guestService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/bookings")
    @Operation(summary = "Отримати бронювання гостя",
            description = "Повертає список усіх бронювань, які зробив вказаний гість.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список бронювань гостя",
                    content = @Content(schema = @Schema(implementation = BookingResponse.class))),
            @ApiResponse(responseCode = "404", description = "Гостя з вказаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<List<BookingResponse>> getGuestBookings(@PathVariable Long id) {
        return ResponseEntity.ok(guestService.getBookings(id));
    }
}
