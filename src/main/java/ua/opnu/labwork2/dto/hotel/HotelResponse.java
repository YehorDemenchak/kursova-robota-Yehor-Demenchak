package ua.opnu.labwork2.dto.hotel;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Дані готелю, що повертаються клієнту")
public record HotelResponse(
        @Schema(description = "Унікальний ідентифікатор готелю", example = "1")
        Long id,
        @Schema(description = "Назва готелю", example = "Hilton")
        String name,
        @Schema(description = "Місто", example = "Київ")
        String city,
        @Schema(description = "Адреса", example = "вул. Хрещатик, 1")
        String address,
        @Schema(description = "Рейтинг (зірковість)", example = "5")
        Integer rating
) {}
