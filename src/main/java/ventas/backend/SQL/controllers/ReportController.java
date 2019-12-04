package ventas.backend.SQL.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ventas.backend.SQL.dto.ReportDto;
import ventas.backend.SQL.services.ReportService;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<List<ReportDto>> getAllReports(){
        try{
            return ResponseEntity.ok(reportService.getAllReports());
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ReportDto> getReportById (@PathVariable("id") Integer id){
        try{
            return ResponseEntity.ok(reportService.getReportById(id));
        }catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity createReport (@RequestBody ReportDto reportDto){
        try{
            return ResponseEntity.ok(reportService.createReport(reportDto));
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity updateReport (@PathVariable("id") Integer id, @RequestBody ReportDto reportDto){
        try{
            reportService.updateReport(reportDto, id);
            return ResponseEntity.ok(HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity deleteReport (@PathVariable Integer id){
        try{
            reportService.deleteReport(id);
            return ResponseEntity.ok(HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}