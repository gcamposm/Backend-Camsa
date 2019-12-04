package ventas.backend.SQL.mappers;

import ventas.backend.SQL.dto.StatisticsDto;
import ventas.backend.Mongo.models.Statistics.Statistics;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
@Component
public class StatisticsMapper {
    public Statistics mapToModel(StatisticsDto statisticsDto){
        Statistics statistics = new Statistics();
        statistics.setId(statisticsDto.getId());
        statistics.setDescription(statisticsDto.getDescription());
        statistics.setType(statisticsDto.getType());
        statistics.setObjectId(statisticsDto.getObjectId());
        statistics.setUserId(statisticsDto.getUserId());
        return statistics;
    }

    public StatisticsDto mapToDto(Statistics statistics){
        StatisticsDto statisticsDto = new StatisticsDto();
        statisticsDto.setId(statistics.getId());
        statisticsDto.setDescription(statistics.getDescription());
        statisticsDto.setType(statistics.getType());
        statisticsDto.setObjectId(statistics.getObjectId());
        statisticsDto.setUserId(statistics.getUserId());
        return statisticsDto;
    }

    public List<StatisticsDto> mapToDtoList(List<Statistics> statisticsList){
        List<StatisticsDto> statisticsDtoArrayList = new ArrayList<>();
        for (Statistics statistics: statisticsList
        ) {
            StatisticsDto StatisticsDto = new StatisticsDto();
            StatisticsDto = mapToDto(statistics);
            statisticsDtoArrayList.add(StatisticsDto);
        }
        return statisticsDtoArrayList;
    }
}