package ua.opnu.labwork2.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "Єдиний формат відповіді про помилку для всіх HTTP-статусів 4xx та 5xx")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorResponse(

        @Schema(description = "Час виникнення помилки на сервері",
                example = "2026-06-10T18:42:11.123")
        LocalDateTime timestamp,

        @Schema(description = "HTTP-статус код", example = "400")
        int status,

        @Schema(description = "Назва HTTP-помилки (стандартне формулювання)", example = "Bad Request")
        String error,

        @Schema(description = "Загальне повідомлення про помилку зрозумілою мовою",
                example = "Помилка валідації вхідних даних")
        String message,

        @Schema(description = "URL запиту, під час якого виникла помилка", example = "/hotels")
        String path,

        @Schema(description = "Помилки валідації за полями (заповнюється лише для 400 з невалідним DTO). " +
                "Ключ - назва поля, значення - текст помилки.",
                example = "{\"name\": \"name є обов'язковим і не може бути порожнім\", " +
                        "\"rating\": \"rating не може бути більшим за 5\"}")
        Map<String, String> errors
) {
}
