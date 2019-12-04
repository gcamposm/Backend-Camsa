package tingeso.backend.SQL.dto;

import tingeso.backend.SQL.models.Desk;
import lombok.Data;
import java.util.List;

@Data
public class PhysicalDeskDto {
    private Integer id;
    private String description;
    private String office;
    private List<Desk> deskList;
}