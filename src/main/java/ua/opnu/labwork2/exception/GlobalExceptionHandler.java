package ua.opnu.labwork2.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ua.opnu.labwork2.dto.common.ApiErrorResponse;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex,
                                                             HttpServletRequest request) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.putIfAbsent(fe.getField(),
                    fe.getDefaultMessage() == null ? "невалідне значення" : fe.getDefaultMessage());
        }
        return build(HttpStatus.BAD_REQUEST, "Помилка валідації вхідних даних",
                request.getRequestURI(), fieldErrors);
    }

    // Некоректний тип параметра
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                               HttpServletRequest request) {
        String msg = "Невірний тип параметра '" + ex.getName() + "'";
        return build(HttpStatus.BAD_REQUEST, msg, request.getRequestURI(), null);
    }

    // Некоректний JSON у тілі запиту
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleNotReadable(HttpMessageNotReadableException ex,
                                                              HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "Некоректне тіло запиту", request.getRequestURI(), null);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(BadRequestException ex,
                                                             HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI(), null);
    }

    // 404

    @ExceptionHandler({ResourceNotFoundException.class, NoSuchElementException.class})
    public ResponseEntity<ApiErrorResponse> handleNotFound(RuntimeException ex,
                                                           HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(), null);
    }

    // 409

    @ExceptionHandler({DuplicateResourceException.class, ConflictOperationException.class})
    public ResponseEntity<ApiErrorResponse> handleConflict(RuntimeException ex,
                                                           HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI(), null);
    }

    // Конфлікт на рівні БД
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex,
                                                                HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, "Порушення цілісності даних", request.getRequestURI(), null);
    }

    // 500

    @ExceptionHandler(DatabaseOperationException.class)
    public ResponseEntity<ApiErrorResponse> handleDb(DatabaseOperationException ex,
                                                    HttpServletRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR,
                "Помилка під час роботи з базою даних: " + ex.getMessage(),
                request.getRequestURI(), null);
    }

    // Узагальнений запобіжний обробник
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleAny(Exception ex, HttpServletRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR,
                "Внутрішня помилка сервера: " + ex.getMessage(),
                request.getRequestURI(), null);
    }

    private ResponseEntity<ApiErrorResponse> build(HttpStatus status, String message,
                                                   String path, Map<String, String> errors) {
        ApiErrorResponse body = new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                errors
        );
        return ResponseEntity.status(status).body(body);
    }
}
