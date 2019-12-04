package tingeso.backend.SQL.mappers;

import org.springframework.stereotype.Component;
import tingeso.backend.SQL.dto.ReportDto;
import tingeso.backend.SQL.models.Report;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
@Component
public class ReportMapper {

    public Report mapToModel(ReportDto reportDto){
        Report report = new Report();
        report.setId(reportDto.getId());
        report.setNameDriver(reportDto.getNameDriver());
        report.setDestiny(reportDto.getDestiny());
        report.setAmount(reportDto.getAmount());
        report.setReceived(reportDto.getReceived());
        report.setExchange(reportDto.getExchange());
        report.setDate(reportDto.getDate());
        return report;
    }

    public List<ReportDto> mapToDtoArrayList(List<Report> reportList) {
        int i;
        List<ReportDto> reportDtoArrayList = new ArrayList<>();
        for(i=0; i< reportList.size(); i++){
            reportDtoArrayList.add(mapToDto(reportList.get(i)));
        }
        return reportDtoArrayList;
    }

    public ReportDto mapToDto (Report report){
        ReportDto reportDto = new ReportDto();
        reportDto.setId(report.getId());
        reportDto.setNameDriver(report.getNameDriver());
        reportDto.setDestiny(report.getDestiny());
        reportDto.setAmount(report.getAmount());
        reportDto.setReceived(report.getReceived());
        reportDto.setExchange(report.getExchange());
        reportDto.setDate(report.getDate());
        return reportDto;
    }
}