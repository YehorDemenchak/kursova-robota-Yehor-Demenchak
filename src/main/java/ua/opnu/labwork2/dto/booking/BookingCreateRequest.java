package ua.opnu.labwork2.dto.booking;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import ua.opnu.labwork2.entity.BookingStatus;

import java.time.LocalDate;

@Schema(description = "Запит на створення нового бронювання. " +
        "guestId і roomId передаються як query-параметри.")
public record BookingCreateRequest(

        @Schema(description = "Дата заїзду (формат ISO YYYY-MM-DD)", example = "2026-05-01",
                requiredMode = Schema.RequiredMode.REQUIRED, format = "date")
        @NotNull(message = "checkInDate є обов'язковим")
        LocalDate checkInDate,

        @Schema(description = "Дата виїзду. Якщо вказана, має бути не раніше checkInDate.",
                example = "2026-05-05", format = "date",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        LocalDate checkOutDate,

        @Schema(description = "Статус бронювання",
                example = "CONFIRMED",
                requiredMode = Schema.RequiredMode.REQUIRED,
                allowableValues = {"PENDING", "CONFIRMED", "ACTIVE", "COMPLETED", "CANCELLED"})
        @NotNull(message = "status є обов'язковим")
        BookingStatus status
) {}
