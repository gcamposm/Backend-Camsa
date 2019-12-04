package tingeso.backend.SQL.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "product_attribute")
public class ProductAttribute {
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
    private Attribute attribute;

    @NonNull
    private String value;

    @OneToMany(mappedBy = "productAttribute", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Stock> stockList;

}
