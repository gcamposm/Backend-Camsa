package tingeso.backend.SQL.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "entry")
public class Entry {
    // ATTRIBUTES
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column(name = "date")
    private LocalDateTime date;

    @NonNull
    private String type;


    private Integer ticketNumber;

    // RELATIONS
    private BigDecimal total;
    // entry -> entryDetail
    @OneToMany(mappedBy = "entry", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<EntryDetail> entryDetailList;

    @ManyToOne
    @JoinColumn
    private User userventas;
}
