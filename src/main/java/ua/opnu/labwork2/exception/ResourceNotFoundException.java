package ua.opnu.labwork2.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
    public ResourceNotFoundException(String entity, Long id) {
        super(entity + " з id=" + id + " не знайдено");
    }
}
