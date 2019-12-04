package tingeso.backend.SQL.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SaleDayStat {
    private LocalDateTime day;
    private String formattedDate;
    private Integer credito;
    private Integer debito;
    private Integer efectivo;
    private Integer transferencia;
    private Integer total;

}
