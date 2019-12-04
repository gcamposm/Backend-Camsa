package ventas.backend.Mongo.dao;

import ventas.backend.Mongo.models.Statistics.DeskStatistic;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeskStatisticDao extends MongoRepository<DeskStatistic, String> {

}
