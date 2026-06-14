package ua.opnu.labwork2.dto.booking;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import ua.opnu.labwork2.entity.BookingStatus;

import java.time.LocalDate;

@Schema(description = "Запит на оновлення бронювання")
public record BookingUpdateRequest(

        @Schema(description = "Дата заїзду", example = "2026-05-02",
                requiredMode = Schema.RequiredMode.REQUIRED, format = "date")
        @NotNull(message = "checkInDate є обов'язковим")
        LocalDate checkInDate,

        @Schema(description = "Дата виїзду (опціональна; якщо вказана, не раніше checkInDate)",
                example = "2026-05-06", format = "date",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        LocalDate checkOutDate,

        @Schema(description = "Новий статус бронювання", example = "ACTIVE",
                requiredMode = Schema.RequiredMode.REQUIRED,
                allowableValues = {"PENDING", "CONFIRMED", "ACTIVE", "COMPLETED", "CANCELLED"})
        @NotNull(message = "status є обов'язковим")
        BookingStatus status
) {}
