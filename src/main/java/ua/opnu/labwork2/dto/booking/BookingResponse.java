package ua.opnu.labwork2.dto.booking;

import io.swagger.v3.oas.annotations.media.Schema;
import ua.opnu.labwork2.entity.BookingStatus;

import java.time.LocalDate;

@Schema(description = "Дані бронювання, що повертаються клієнту")
public record BookingResponse(
        @Schema(description = "Унікальний ідентифікатор бронювання", example = "1")
        Long id,
        @Schema(description = "Дата заїзду", example = "2026-05-01", format = "date")
        LocalDate checkInDate,
        @Schema(description = "Дата виїзду", example = "2026-05-05", format = "date")
        LocalDate checkOutDate,
        @Schema(description = "Поточний статус бронювання", example = "CONFIRMED")
        BookingStatus status
) {}
