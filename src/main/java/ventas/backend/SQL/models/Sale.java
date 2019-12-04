package ventas.backend.SQL.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "sale")
public class Sale {
    // ATTRIBUTES
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime date;

    private String formatDate;

    private String voucherType;

    @NonNull
    private BigDecimal total;

    @NonNull
    private BigDecimal totalWithDiscount;

    @NonNull
    private BigDecimal totalDiscount;

    private BigDecimal positiveBalance;

    @NonNull
    private Integer totalQuantity;

    @NonNull
    private Integer ticketNumber;

    @Column(columnDefinition="text")
    private String description;

    @NonNull
    private Boolean finalized;
    // RELATIONS

    //sale -> saleDetail
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    private List<SaleDetail> saleDetailList;

    //sale -> payment_sale
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SalePaymentMethod> salePaymentMethodList;

    //user -> sale
    @NonNull
    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private User userventas;

    //desk -> sale
    @NonNull
    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Desk desk;

    public void deleteAllSaleDetails(){
        this.setSaleDetailList(new ArrayList<>());
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", date=" + date +
                ", ticketNumber=" + ticketNumber +
                ", finalized=" + finalized +
                '}';
    }
}
