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
import ua.opnu.labwork2.dto.booking.BookingCreateRequest;
import ua.opnu.labwork2.dto.booking.BookingResponse;
import ua.opnu.labwork2.dto.booking.BookingUpdateRequest;
import ua.opnu.labwork2.dto.common.ApiErrorResponse;
import ua.opnu.labwork2.service.BookingService;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@Tag(name = "Бронювання",
        description = "Управління бронюваннями: створення, оновлення статусу, видалення, перегляд активних бронювань")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @Operation(summary = "Створити бронювання",
            description = "Створює нове бронювання для вказаного гостя та номера. " +
                    "Перевіряє: checkInDate (обов'язкове), checkOutDate (опціональне, але якщо вказане, " +
                    "не може бути раніше за checkInDate), status (одне з PENDING, CONFIRMED, ACTIVE, COMPLETED, CANCELLED). " +
                    "Бізнес-правила: пов'язані гість і номер мають існувати; для активних бронювань " +
                    "(PENDING/CONFIRMED/ACTIVE) перевіряється відсутність перетину дат із вже існуючими.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Бронювання створено",
                    content = @Content(schema = @Schema(implementation = BookingResponse.class))),
            @ApiResponse(responseCode = "400", description = "Помилка валідації або checkOutDate раніше за checkInDate",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Гість або номер з вказаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Номер уже має активне бронювання з перетином дат",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingCreateRequest request,
                                                         @RequestParam(required = false) Long guestId,
                                                         @RequestParam(required = false) Long roomId) {
        return ResponseEntity.ok(bookingService.create(request, guestId, roomId));
    }

    @GetMapping
    @Operation(summary = "Отримати всі бронювання",
            description = "Повертає повний список бронювань у системі.")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Список бронювань",
            content = @Content(schema = @Schema(implementation = BookingResponse.class))))
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        return ResponseEntity.ok(bookingService.findAll());
    }

    @GetMapping("/active")
    @Operation(summary = "Отримати активні бронювання",
            description = "Повертає список бронювань зі статусом ACTIVE (гість зараз заселений).")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Список активних бронювань",
            content = @Content(schema = @Schema(implementation = BookingResponse.class))))
    public ResponseEntity<List<BookingResponse>> getActiveBookings() {
        return ResponseEntity.ok(bookingService.findActive());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Отримати бронювання за id",
            description = "Повертає інформацію про конкретне бронювання.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Бронювання знайдено",
                    content = @Content(schema = @Schema(implementation = BookingResponse.class))),
            @ApiResponse(responseCode = "404", description = "Бронювання з вказаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Оновити бронювання",
            description = "Оновлює бронювання (дати та статус, опціонально - пов'язаних гостя/номер). " +
                    "Перевірка перетину дат із іншими активними бронюваннями цього самого номера зберігається.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Бронювання оновлено",
                    content = @Content(schema = @Schema(implementation = BookingResponse.class))),
            @ApiResponse(responseCode = "400", description = "Помилка валідації або checkOutDate раніше за checkInDate",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Бронювання, гість або номер не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Перетин дат з іншим активним бронюванням",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<BookingResponse> updateBooking(@PathVariable Long id,
                                                         @Valid @RequestBody BookingUpdateRequest request,
                                                         @RequestParam(required = false) Long guestId,
                                                         @RequestParam(required = false) Long roomId) {
        return ResponseEntity.ok(bookingService.update(id, request, guestId, roomId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Видалити бронювання",
            description = "Безумовно видаляє бронювання за його id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Бронювання успішно видалено"),
            @ApiResponse(responseCode = "404", description = "Бронювання з вказаним id не знайдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
