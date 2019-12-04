package ventas.backend.SQL.dto;

import ventas.backend.SQL.models.EntryDetail;
import ventas.backend.SQL.models.Sale;
import ventas.backend.SQL.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class EntryDto {
    private Integer id;
    private LocalDateTime date;
    private String type;
    private BigDecimal total;
    @JsonIgnore
    private Sale sale;
    private Integer ticketNumber;
    private String formattedDate;
    private List<EntryDetail> entryDetailList;
    private User userventas;
}
