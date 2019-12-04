package ventas.backend.SQL.controllers;

import ventas.backend.SQL.dto.DeskDto;
import ventas.backend.SQL.services.DeskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/desks")
public class DeskController {
    @Autowired
    private DeskService deskService;

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<List<DeskDto>> getAllDesk(){
        try{
            return ResponseEntity.ok(deskService.getAllDesk());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<DeskDto> findDeskById (@PathVariable("id") Integer id){

        try{
            return ResponseEntity.ok(deskService.getDeskById(id));
        }

        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity create (@RequestBody DeskDto deskDto){

        try{
            return ResponseEntity.ok(deskService.createDesk(deskDto));
        }

        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/newDesk", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity createNewDesk(@RequestParam("userId") Integer userId,
                                        @RequestParam("initialMoney") Integer initialMoney,
                                        @RequestParam("physicalDeskId") Integer physicalDeskId){
        try{
            DeskDto deskDto = deskService.createNewDesk(userId, initialMoney, physicalDeskId);
            if(deskDto != null) {
                return ResponseEntity.ok(deskDto);
            } else{
               return ResponseEntity
                       .status(HttpStatus.BAD_REQUEST)
                       .body("Ya existe una caja abierta bajo este usuario o modulo, favor cerrar las sesiones pendientes");
            }
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/addMoney", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity addMoney(@RequestParam("userId") Integer userId,
                                   @RequestParam("moneyToAdd") Integer moneyToAdd,
                                   @RequestParam("physicalDeskId") Integer deskId){
        try{
            DeskDto deskDto = deskService.addMoney(userId, moneyToAdd, deskId);
            if(deskDto != null) {
                return ResponseEntity.ok(deskDto);
            } else{
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El usuario no posee una caja abierta.");
            }
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/closeDesk", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity closeDesk(@RequestParam("userId") Integer userId,
                                    @RequestParam("withdraw") Integer withdraw,
                                    @RequestParam("physicalDeskId") Integer deskId){
        try{
            DeskDto deskDto = deskService.closeDesk(userId, withdraw, deskId);
            if(deskDto != null) {
                return ResponseEntity.ok(deskDto);
            } else{
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("No existe una caja abierta registrada");
            }
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity update (@PathVariable("id") Integer id,
                                  @RequestBody DeskDto deskDto){

        try{
            deskService.updateDesk(deskDto, id);
            return ResponseEntity.ok(HttpStatus.OK);
        }

        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity delete (@PathVariable Integer id){

        try{
            deskService.deleteDesk(id);
            return ResponseEntity.ok(HttpStatus.OK);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/actualDesk/", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity actualDesk (@RequestParam("userId") Integer userId,
                                      @RequestParam("physicalDeskId") Integer physicalDeskId){
        try{
            DeskDto deskDto = deskService.actualDesk(userId, physicalDeskId);
            if(deskDto == null)
            {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No existe una caja abierta para el usuario indicado.");
            }
            return ResponseEntity.ok(deskDto);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/lastDesk/{id}")
    @ResponseBody
    public ResponseEntity lastDesk (@PathVariable("id") Integer id){
        try {
            DeskDto deskDto = deskService.getLastDesk(id);
            if(deskDto == null)
            {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No existe una caja abierta.");
            }
            return ResponseEntity.ok(deskDto);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("No existen cajas anteriores");
        }
    }
    @GetMapping("/actualizeTotal/user/{userId}/physicalDesk/{physicalDeskId}")
    @ResponseBody
    public ResponseEntity actualizeTotal (@PathVariable("userId") Integer userId,
                                          @PathVariable("physicalDeskId") Integer physicalDeskId){
        try {
            return ResponseEntity.ok(deskService.actualizeTotal(userId, physicalDeskId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Ha ocurrido un error al calcular el total");
        }
    }
    /*@RequestMapping(value = "/totals", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity totalsByDesk (@RequestParam("userId") Integer userId,
                                      @RequestParam("physicalDeskId") Integer physicalDeskId){
        try{
            Object totals = deskService.totalsByDesk(userId, physicalDeskId);
            if(totals == null)
            {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No existe una caja abierta para el usuario indicado.");
            }
            return ResponseEntity.ok(totals);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }*/
    @GetMapping("/calculateTotal/{deskId}")
    @ResponseBody
    public ResponseEntity totalsByDesk (@PathVariable("deskId") int deskId){
        try{
            Object totals = deskService.totalByDeskId(deskId);
            if(totals == null)
            {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No existe una caja abierta para el usuario indicado.");
            }
            return ResponseEntity.ok(totals);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/getCashInLastDesk/{physicalDeskId}")
    @ResponseBody
    public ResponseEntity getCashInLastDesk(@PathVariable("physicalDeskId") int physicalDeskId){
        try{
            Integer response =  deskService.getTotalByIsOpenedAndPhysicalDeskId(physicalDeskId);
            if(response != null){
                return ResponseEntity.ok(response);
            }
            else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("No se ha podido procesar la información");
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e);
        }
    }
}