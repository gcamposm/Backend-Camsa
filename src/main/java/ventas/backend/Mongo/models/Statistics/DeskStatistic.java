package ventas.backend.Mongo.models.Statistics;

import ventas.backend.Email.Report;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document(collection = "desk_statistic")
@Data
public class DeskStatistic {
    @Id
    private String id;
    private Report report;

    public DeskStatistic(Report report) {
        this.report = report;
    }
}
