package ua.opnu.labwork2.dto.room;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Запит на створення нового номера готелю")
public record RoomCreateRequest(

        @Schema(description = "Номер кімнати (символьний код, не обов'язково число)", example = "101",
                requiredMode = Schema.RequiredMode.REQUIRED, minLength = 2, maxLength = 30)
        @NotBlank(message = "number є обов'язковим і не може бути порожнім")
        @Size(min = 2, max = 30, message = "number має містити від 2 до 30 символів")
        String number,

        @Schema(description = "Тип номера (один з допустимих значень)",
                example = "Deluxe",
                allowableValues = {"Standard", "Deluxe", "Suite", "Family", "Presidential"},
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "type є обов'язковим")
        @Pattern(regexp = "Standard|Deluxe|Suite|Family|Presidential",
                message = "type має бути одним з: Standard, Deluxe, Suite, Family, Presidential")
        String type,

        @Schema(description = "Вартість за добу в гривнях", example = "1500.0",
                requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
        @NotNull(message = "pricePerNight є обов'язковим")
        @DecimalMin(value = "0.0", message = "pricePerNight не може бути від'ємним")
        Double pricePerNight,

        @Schema(description = "Максимальна кількість гостей у номері", example = "2",
                requiredMode = Schema.RequiredMode.REQUIRED, exclusiveMinimum = true, minimum = "0")
        @NotNull(message = "capacity є обов'язковим")
        @Positive(message = "capacity має бути додатним числом")
        Integer capacity
) {}
