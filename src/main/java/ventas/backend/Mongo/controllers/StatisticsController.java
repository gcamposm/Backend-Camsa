package ventas.backend.Mongo.controllers;

import ventas.backend.Mongo.models.Statistics.Statistics;
import ventas.backend.Mongo.services.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/statisticsClicks")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<List<Statistics>> findCategoryById (){
        try{
            return ResponseEntity.ok(statisticsService.findAll());
        } catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Statistics> findCategoryById (@PathVariable("id") String id){
        try{
            return ResponseEntity.ok(statisticsService.getStatistics(id));
        } catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<Statistics> creteNewStatistics(){
        try{
            return ResponseEntity.ok(statisticsService.createStatistics());
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}