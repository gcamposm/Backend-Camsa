package tingeso.backend.Exceptions;

public class CreateSaleException extends RuntimeException {
    public CreateSaleException(String message) {
        super(message);
    }

    public CreateSaleException(String message, Throwable cause) {
        super(message, cause);
    }
}
