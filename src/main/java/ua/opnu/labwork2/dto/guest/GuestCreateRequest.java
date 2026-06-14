package ua.opnu.labwork2.dto.guest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Запит на реєстрацію нового гостя")
public record GuestCreateRequest(

        @Schema(description = "Ім'я гостя", example = "Іван",
                requiredMode = Schema.RequiredMode.REQUIRED, minLength = 2, maxLength = 50)
        @NotBlank(message = "firstName є обов'язковим і не може бути порожнім")
        @Size(min = 2, max = 50, message = "firstName має містити від 2 до 50 символів")
        String firstName,

        @Schema(description = "Прізвище гостя", example = "Коваль",
                requiredMode = Schema.RequiredMode.REQUIRED, minLength = 2, maxLength = 50)
        @NotBlank(message = "lastName є обов'язковим і не може бути порожнім")
        @Size(min = 2, max = 50, message = "lastName має містити від 2 до 50 символів")
        String lastName,

        @Schema(description = "Електронна пошта (унікальна в межах системи)",
                example = "ivan.koval@example.com",
                requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 100, format = "email")
        @NotBlank(message = "email є обов'язковим")
        @Email(message = "email має відповідати формату email")
        @Size(max = 100, message = "email не може бути довшим за 100 символів")
        String email,

        @Schema(description = "Номер телефону (цифри, пробіли та символ +)",
                example = "+380501234567",
                requiredMode = Schema.RequiredMode.REQUIRED, minLength = 10, maxLength = 20,
                pattern = "^[0-9+ ]+$")
        @NotBlank(message = "phone є обов'язковим")
        @Size(min = 10, max = 20, message = "phone має містити від 10 до 20 символів")
        @Pattern(regexp = "^[0-9+ ]+$",
                message = "phone може містити лише цифри, пробіли та символ +")
        String phone
) {}
