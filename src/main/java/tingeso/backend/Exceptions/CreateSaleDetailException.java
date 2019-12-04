package tingeso.backend.Exceptions;

public class CreateSaleDetailException extends RuntimeException {
    public CreateSaleDetailException(String message) {
        super(message);
    }

    public CreateSaleDetailException(String message, Throwable cause) {
        super(message, cause);
    }
}
