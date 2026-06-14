package ua.opnu.labwork2.dto.hotel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Запит на оновлення даних готелю")
public record HotelUpdateRequest(

        @Schema(description = "Нова назва готелю", example = "Hilton Updated",
                requiredMode = Schema.RequiredMode.REQUIRED, minLength = 2, maxLength = 150)
        @NotBlank(message = "name є обов'язковим і не може бути порожнім")
        @Size(min = 2, max = 150, message = "name має містити від 2 до 150 символів")
        String name,

        @Schema(description = "Місто", example = "Київ",
                requiredMode = Schema.RequiredMode.REQUIRED, minLength = 2, maxLength = 150)
        @NotBlank(message = "city є обов'язковим і не може бути порожнім")
        @Size(min = 2, max = 150, message = "city має містити від 2 до 150 символів")
        String city,

        @Schema(description = "Поштова адреса", example = "вул. Хрещатик, 1",
                requiredMode = Schema.RequiredMode.REQUIRED, minLength = 5, maxLength = 200)
        @NotBlank(message = "address є обов'язковим і не може бути порожнім")
        @Size(min = 5, max = 200, message = "address має містити від 5 до 200 символів")
        String address,

        @Schema(description = "Рейтинг", example = "5",
                requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0", maximum = "5")
        @NotNull(message = "rating є обов'язковим")
        @Min(value = 0, message = "rating не може бути меншим за 0")
        @Max(value = 5, message = "rating не може бути більшим за 5")
        Integer rating
) {}
