package ventas.backend.SQL.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "provider")
public class Provider {
    // ATTRIBUTES
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column(name = "name")
    private String name;

    private Boolean deleted;
    // RELATIONS
    // provider -> product
    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Product> productList;
}