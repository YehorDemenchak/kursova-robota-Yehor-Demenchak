package ua.opnu.labwork2.dto.amenity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Дані зручності, що повертаються клієнту")
public record AmenityResponse(
        @Schema(description = "Унікальний ідентифікатор зручності", example = "1")
        Long id,
        @Schema(description = "Назва зручності", example = "Wi-Fi")
        String name,
        @Schema(description = "Опис зручності",
                example = "Безкоштовний бездротовий інтернет у всіх зонах готелю")
        String description
) {}
