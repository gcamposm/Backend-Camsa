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
@Table(name = "site")
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    private String code;

    private String name;

    private Boolean deleted;

    //product -> saleDetail
    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Stock> stockList;
    // RELATIONS
    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PhysicalDesk> physicalDeskList;
}
