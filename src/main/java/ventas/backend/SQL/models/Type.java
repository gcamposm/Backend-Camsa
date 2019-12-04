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
@Table(name = "type")
public class Type {
    // ATTRIBUTES
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column(name = "type")
    private String type;

    // RELATIONS

    // type -> product
    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Product> productList;
}
