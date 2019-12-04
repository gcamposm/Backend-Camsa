package tingeso.backend.SQL.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductSoldDto {
    private Integer id;
    private String code;
    private BigDecimal cost;
    private BigDecimal soldPrice;
    private Integer totalSoldQuantity;
    private String productName;
}
