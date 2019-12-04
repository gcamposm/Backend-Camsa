package ventas.backend.SQL.dto;

import ventas.backend.SQL.models.PhysicalDesk;
import ventas.backend.SQL.models.Sale;
import ventas.backend.SQL.models.User;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DeskDto {
    private Integer id;
    private Integer total;
    private LocalDateTime dateOpen;
    private LocalDateTime dateClose;
    private Boolean isOpen;
    private int initialMoney;
    private int withdraw;
    // RELATIONS
    private User userventas;
    private PhysicalDesk physicalDesk;
    private List<Sale> saleList;
}
