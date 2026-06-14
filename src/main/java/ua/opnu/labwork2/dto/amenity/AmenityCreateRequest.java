package ua.opnu.labwork2.dto.amenity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Запит на створення нової зручності")
public record AmenityCreateRequest(

        @Schema(description = "Назва зручності", example = "Wi-Fi",
                requiredMode = Schema.RequiredMode.REQUIRED, minLength = 2, maxLength = 150)
        @NotBlank(message = "name є обов'язковим і не може бути порожнім")
        @Size(min = 2, max = 150, message = "name має містити від 2 до 150 символів")
        String name,

        @Schema(description = "Розгорнутий опис зручності",
                example = "Безкоштовний бездротовий інтернет у всіх зонах готелю",
                requiredMode = Schema.RequiredMode.REQUIRED, minLength = 10, maxLength = 2000)
        @NotBlank(message = "description є обов'язковим")
        @Size(min = 10, max = 2000, message = "description має містити від 10 до 2000 символів")
        String description
) {}
