package ventas.backend.Exceptions;

public class CalculateSaleTotalsException extends RuntimeException{
    public CalculateSaleTotalsException(String message) {
        super(message);
    }

    public CalculateSaleTotalsException(String message, Throwable cause) {
        super(message, cause);
    }
}
