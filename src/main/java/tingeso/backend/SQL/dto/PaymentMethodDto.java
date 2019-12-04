package tingeso.backend.SQL.dto;

import tingeso.backend.SQL.models.SalePaymentMethod;
import lombok.Data;
import java.util.List;

@Data
public class PaymentMethodDto {
    private Integer id;
    private String type;
    private List<SalePaymentMethod> salePaymentMethodList;
}