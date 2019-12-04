package ventas.backend.SQL.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "desk")
public class Desk {
    // ATTRIBUTES
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NonNull
    private int total;
    @NonNull
    private LocalDateTime dateOpen;
    private LocalDateTime dateClose;
    @NonNull
    private Boolean isOpen;
    @NonNull
    private int initialMoney;
    @NonNull
    private int withdraw;

    // RELATIONS

    @ManyToOne
    @JoinColumn
    private User userventas;

    @ManyToOne
    @JoinColumn
    private PhysicalDesk physicalDesk;

    @OneToMany(mappedBy = "desk", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Sale> saleList;

    public void calculateTotal(){
        this.total = initialMoney;
        if(saleList.size() > 0) {
            this.saleList.forEach(sale -> {
                if(sale.getFinalized() == true) {
                    total += sale.getTotalWithDiscount().intValue();
                }
            });
        }
    }

    @Override
    public String toString() {
        return "Desk{" +
                "id=" + id +
                ", total=" + total +
                ", dateOpen=" + dateOpen +
                ", dateClose=" + dateClose +
                ", isOpen=" + isOpen +
                ", initialMoney=" + initialMoney +
                ", withdraw=" + withdraw +
                '}';
    }
}
