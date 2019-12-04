package tingeso.backend.SQL.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@Table(name = "entry_detail")
public class EntryDetail {
    // ATTRIBUTES
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    private Integer quantity;

    @NonNull
    private String type;

    private Boolean sumStock;
    // RELATIONS
    private BigDecimal subTotal;
    // product -> entryDetail
    @ManyToOne
    @JoinColumn
    private Product product;

    // entry -> entryDetail
    @JsonIgnore
    @ManyToOne
    @JoinColumn
    private Entry entry;
}