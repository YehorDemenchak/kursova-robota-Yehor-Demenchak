package ua.opnu.labwork2.dto.room;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Дані номера готелю, що повертаються клієнту")
public record RoomResponse(
        @Schema(description = "Унікальний ідентифікатор номера", example = "1")
        Long id,
        @Schema(description = "Номер кімнати", example = "101")
        String number,
        @Schema(description = "Тип номера", example = "Deluxe")
        String type,
        @Schema(description = "Вартість за добу", example = "1500.0")
        Double pricePerNight,
        @Schema(description = "Місткість", example = "2")
        Integer capacity
) {}
