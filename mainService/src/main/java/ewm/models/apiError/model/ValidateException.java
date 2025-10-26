package ewm.models.apiError.model;

public class ValidateException extends RuntimeException {
    public ValidateException(String message) {
        super(message);
    }
}
