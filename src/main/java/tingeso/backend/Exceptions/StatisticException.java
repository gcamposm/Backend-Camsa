package tingeso.backend.Exceptions;

public class StatisticException extends RuntimeException {
    public StatisticException(String message) {
        super(message);
    }

    public StatisticException(String message, Throwable cause) {
        super(message, cause);
    }
}
