package ventas.backend.SQL.dto;

import ventas.backend.SQL.models.Desk;
import lombok.Data;
import java.util.List;

@Data
public class PhysicalDeskDto {
    private Integer id;
    private String description;
    private String office;
    private List<Desk> deskList;
}