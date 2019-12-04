package ventas.backend.SQL.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReducedSaleDetailDto {
    private int productId;
    private int quantity;
    private String name;
    private BigDecimal discount;
    private BigDecimal subTotal;
    private BigDecimal unitaryPrice;
    private BigDecimal subTotalWithDiscount;
}
