package tingeso.backend.SQL.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
//@JsonIgnoreProperties("product")
@Table(name = "sale_payment_method")
public class SalePaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer amount;
    private Integer exchange;
    @ManyToOne
    @JoinColumn
    private Sale sale;
    @ManyToOne
    @JoinColumn
    private PaymentMethod paymentMethod;
}