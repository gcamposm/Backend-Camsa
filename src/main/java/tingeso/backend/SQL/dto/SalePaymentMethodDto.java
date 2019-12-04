package tingeso.backend.SQL.dto;

import tingeso.backend.SQL.models.PaymentMethod;
import tingeso.backend.SQL.models.Sale;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class SalePaymentMethodDto {
    private Integer id;
    private Integer amount;
    private Integer exchange;
    @JsonIgnore
    private Sale sale;
    private PaymentMethod paymentMethod;
}