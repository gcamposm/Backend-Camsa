package ventas.backend.Exceptions;

public class NotSufficientStockException extends RuntimeException{
    public NotSufficientStockException(String message) {
        super(message);
    }

    public NotSufficientStockException(String message, Throwable cause) {
        super(message, cause);
    }
}
