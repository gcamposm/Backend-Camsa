package tingeso.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tingeso.backend.dao.ReportDao;
import tingeso.backend.dto.ReportDto;
import tingeso.backend.mapper.ReportMapper;
import tingeso.backend.model.Report;
import java.util.List;

@Service
public class ReportService {
    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private ReportDao reportDao;

    public List<ReportDto> getAllReports(){
        List<Report> reportList = reportDao.findAll();
        return reportMapper.mapToDtoArrayList(reportList);
    }

    public ReportDto getReportById(Integer id){
        if(reportDao.findById(id).isPresent()){
            return reportMapper.mapToDto(reportDao.findReportById(id));
        }else{
            return  null;
        }
    }

    public ReportDto createReport(ReportDto reportDto){return reportMapper.mapToDto(reportDao.save(reportMapper.mapToModel(reportDto))); }

    public void updateReport(ReportDto reportDto, Integer id){
        if(reportDao.findById(id).isPresent()){
            Report reportFound = reportDao.findReportById(id);
            reportFound.setNameDriver(reportDto.getNameDriver());
            reportFound.setDestiny(reportDto.getDestiny());
            reportFound.setAmount(reportDto.getAmount());
            reportFound.setReceived(reportDto.getReceived());
            reportFound.setExchange(reportDto.getExchange());
            reportFound.setDate(reportDto.getDate());
            reportDao.save(reportFound);
        }
    }

    public void deleteReport(Integer id){
        reportDao.delete(reportDao.findReportById(id));
    }
}