package ventas.backend.Exceptions;

public class CodeNotFoundException extends RuntimeException {
    public CodeNotFoundException(String message) {
        super(message);
    }

    public CodeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
