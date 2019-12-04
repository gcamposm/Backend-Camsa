package ventas.backend.SQL.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleWithPaymentMethodsDto {
    Integer ticketNumber;
    BigDecimal totalWithDiscount;
    Integer amount;
    String type;

    public SaleWithPaymentMethodsDto(Integer ticketNumber, BigDecimal totalWithDiscount, Integer amount, String type) {
        this.ticketNumber = ticketNumber;
        this.totalWithDiscount = totalWithDiscount;
        this.amount = amount;
        this.type = type;
    }
}
