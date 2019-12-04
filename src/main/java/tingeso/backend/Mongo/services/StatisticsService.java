package tingeso.backend.Mongo.services;

import tingeso.backend.Mongo.dao.StatisticsDao;
import tingeso.backend.Mongo.models.Statistics.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StatisticsService {

    @Autowired
    private StatisticsDao statisticsDao;

    public Statistics createStatistics(){
        Statistics statistics = new Statistics();
        return statisticsDao.save(statistics);
    }

    public Statistics getStatistics(String statisticsId){
        return statisticsDao.findStatisticsById(statisticsId);
    }

    public List<Statistics> findAll(){
        return statisticsDao.findAll();
    }
}