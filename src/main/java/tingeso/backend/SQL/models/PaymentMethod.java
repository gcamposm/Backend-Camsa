package tingeso.backend.SQL.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
//@JsonIgnoreProperties("product")
@Table(name = "payment_method")
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String type;

    @OneToMany(mappedBy = "paymentMethod", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SalePaymentMethod> salePaymentMethodList;
}
