package ventas.backend.SQL.dto;

import lombok.Data;

@Data
public class StatisticsDto {
    private String id;
    private String description;
    private Integer userId;
    private String type;
    private Integer objectId;
}
