package ventas.backend.SQL.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@JsonIgnoreProperties("deskList")
@Table(name = "physical_desk")
public class PhysicalDesk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    private String office;

    @ManyToOne
    @JoinColumn
    private Site site;

    @OneToMany(mappedBy = "physicalDesk", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Desk> deskList;
}