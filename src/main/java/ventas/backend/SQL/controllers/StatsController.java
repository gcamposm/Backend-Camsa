package ventas.backend.SQL.controllers;

import ventas.backend.SQL.services.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/stats")
public class StatsController {

    @Autowired
    private StatsService statsService;

    @PostMapping(value = "getTotalsBetweenDates")
    public ResponseEntity getSalesByDate(@RequestParam("firstDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date firstDate,
                                         @RequestParam("secondDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date secondDate){
        return ResponseEntity.ok(statsService.getStats(firstDate, secondDate));
    }
    @PostMapping(value = "mostSoldProductsPerDay")
    public ResponseEntity getMostSoldProductsPerDay(@RequestParam("firstDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date firstDate,
                                                @RequestParam("secondDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date secondDate){
        return ResponseEntity.ok(statsService.getMostSoldProducts(firstDate, secondDate));
    }
    @PostMapping(value = "mostSoldProducts")
    public ResponseEntity getMostSoldProductsInInterval(@RequestParam("firstDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date firstDate,
                                                @RequestParam("secondDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date secondDate){
        return ResponseEntity.ok(statsService.getMostSoldProductsInInterval(firstDate, secondDate));
    }
}
