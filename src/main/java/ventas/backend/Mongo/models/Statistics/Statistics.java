package ventas.backend.Mongo.models.Statistics;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.*;

@Data
@Document(collection = "statistics_clicks")
public class Statistics {
    @Id
    private String id;
    private String description;
    // RELATIONS
    private Integer userId;
    private String type;
    private Integer objectId;
}