package ventas.backend.SQL.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class SaleWithPaymentMethodEncapsulatedDto {
    Integer ticketNumber;
    BigDecimal totalWithDiscount;
    List<SaleWithPaymentMethodsDto> saleWithPaymentMethodsDtoList = new ArrayList<>();

    public SaleWithPaymentMethodEncapsulatedDto(Integer ticketNumber, BigDecimal totalWithDiscount, List<SaleWithPaymentMethodsDto> saleWithPaymentMethodsDtoList) {
        this.ticketNumber = ticketNumber;
        this.totalWithDiscount = totalWithDiscount;
        this.saleWithPaymentMethodsDtoList = saleWithPaymentMethodsDtoList;
    }
}
