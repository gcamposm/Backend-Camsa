package tingeso.backend.Mongo.dao;

import tingeso.backend.Mongo.models.Statistics.Statistics;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StatisticsDao extends MongoRepository<Statistics, Integer> {
    Statistics findStatisticsById(String id);
}
