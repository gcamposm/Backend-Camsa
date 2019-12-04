package tingeso.backend.Mongo.dao;

import tingeso.backend.Mongo.models.Statistics.DeskStatistic;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeskStatisticDao extends MongoRepository<DeskStatistic, String> {

}
