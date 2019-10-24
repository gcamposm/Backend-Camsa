package tingeso.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReportDto {
    private Integer id;
    private String nameDriver;
    private String destiny;
    private String amount;
    private Boolean received;
    private Boolean exchange;
    private String date;
}