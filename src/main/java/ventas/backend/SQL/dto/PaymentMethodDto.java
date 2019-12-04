package ventas.backend.SQL.dto;

import ventas.backend.SQL.models.SalePaymentMethod;
import lombok.Data;
import java.util.List;

@Data
public class PaymentMethodDto {
    private Integer id;
    private String type;
    private List<SalePaymentMethod> salePaymentMethodList;
}