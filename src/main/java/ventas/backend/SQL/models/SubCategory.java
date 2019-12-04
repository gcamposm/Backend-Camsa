package ventas.backend.SQL.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "sub_category")
@JsonIgnoreProperties({"productList"})
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    private String name;

    private Boolean deleted;

    private Boolean published;
    // RELATIONS
    // category -> subCategory
    @ManyToOne
    @JoinColumn
    private Category category;

    // subCategory -> product
    @OneToMany(mappedBy = "subCategory", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Product> productList;
}