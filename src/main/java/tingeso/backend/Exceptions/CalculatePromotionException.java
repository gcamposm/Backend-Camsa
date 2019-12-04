package tingeso.backend.Exceptions;

public class CalculatePromotionException extends RuntimeException {
    public CalculatePromotionException(String message) {
        super(message);
    }

    public CalculatePromotionException(String message, Throwable cause) {
        super(message, cause);
    }
}
