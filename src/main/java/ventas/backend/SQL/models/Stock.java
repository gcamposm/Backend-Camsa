package ventas.backend.SQL.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "stock")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @ManyToOne
    @JoinColumn
    private Product product;

    @NonNull
    @ManyToOne
    @JoinColumn
    private Site site;

    @ManyToOne
    @JoinColumn
    private ProductAttribute productAttribute;

    @NonNull
    private Integer quantity;


}
