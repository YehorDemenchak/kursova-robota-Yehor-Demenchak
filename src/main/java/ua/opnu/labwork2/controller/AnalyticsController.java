package ua.opnu.labwork2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.opnu.labwork2.service.AnalyticsService;

import java.util.Map;

@RestController
@RequestMapping("/analytics")
@Tag(name = "Аналітика", description = "Агреговані показники по системі: підрахунки готелів, номерів, бронювань")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/hotels/count")
    @Operation(summary = "Кількість готелів у системі")
    public ResponseEntity<Long> getHotelsCount() {
        return ResponseEntity.ok(analyticsService.countHotels());
    }

    @GetMapping("/rooms/count")
    @Operation(summary = "Кількість номерів у системі")
    public ResponseEntity<Long> getRoomsCount() {
        return ResponseEntity.ok(analyticsService.countRooms());
    }

    @GetMapping("/bookings/active")
    @Operation(summary = "Кількість активних бронювань")
    public ResponseEntity<Long> getActiveBookingsCount() {
        return ResponseEntity.ok(analyticsService.countActiveBookings());
    }

    @GetMapping("/bookings/completed")
    @Operation(summary = "Кількість завершених бронювань")
    public ResponseEntity<Long> getCompletedBookingsCount() {
        return ResponseEntity.ok(analyticsService.countCompletedBookings());
    }

    @GetMapping("/rooms/by-type")
    @Operation(summary = "Розподіл номерів за типами",
            description = "Повертає мапу: тип номера -> кількість номерів цього типу.")
    public ResponseEntity<Map<String, Long>> getRoomsByType() {
        return ResponseEntity.ok(analyticsService.roomsByType());
    }
}
