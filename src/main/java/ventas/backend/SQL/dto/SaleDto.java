package ventas.backend.SQL.dto;

import ventas.backend.SQL.models.Desk;
import ventas.backend.SQL.models.SaleDetail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SaleDto {
    private Integer id;
    private LocalDateTime date;
    private String formatDate;
    private String voucherType;
    private BigDecimal totalDiscount;
    private Integer totalQuantity;
    private BigDecimal total;
    private BigDecimal positiveBalance;
    private BigDecimal totalWithDiscount;
    private String description;
    private Integer ticketNumber;
    private Boolean finalized;
    // RELATIONS
    // sale -> saleDetail
    private List<SaleDetail> saleDetailList;
    // user -> sale
    private String userName;
    // desk -> sale
    @JsonIgnore
    private Desk desk;
}