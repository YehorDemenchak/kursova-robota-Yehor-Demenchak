package ua.opnu.labwork2.dto.guest;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Дані гостя, що повертаються клієнту")
public record GuestResponse(
        @Schema(description = "Унікальний ідентифікатор гостя", example = "1")
        Long id,
        @Schema(description = "Ім'я", example = "Іван")
        String firstName,
        @Schema(description = "Прізвище", example = "Коваль")
        String lastName,
        @Schema(description = "Електронна пошта", example = "ivan.koval@example.com")
        String email,
        @Schema(description = "Номер телефону", example = "+380501234567")
        String phone
) {}
