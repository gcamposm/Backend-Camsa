package ventas.backend.SQL.Exception;

public class DeskNotFoundExeption extends Exception {
    public DeskNotFoundExeption(){}
    public DeskNotFoundExeption(String message)
    {
        super(message);
    }
}
