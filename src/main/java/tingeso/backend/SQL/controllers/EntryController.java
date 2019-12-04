package tingeso.backend.SQL.controllers;

import tingeso.backend.SQL.dao.SaleDao;
import tingeso.backend.SQL.dto.EntryDto;
import tingeso.backend.SQL.models.Sale;
import tingeso.backend.SQL.services.EntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/entries")
public class EntryController {

    @Autowired
    private SaleDao saleDao;

    @Autowired
    private EntryService entryService;

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<List<EntryDto>> getAllEntry(){
        try{
            return ResponseEntity.ok(entryService.getAllEntry());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<EntryDto> findEntryById (@PathVariable("id") Integer id){

        try{
            return ResponseEntity.ok(entryService.getEntryById(id));
        }

        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/newDevolution", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity createNewSale (@RequestParam("saleId") Integer saleId ,
                                         @RequestParam("userId") Integer userId){

        try{
            return ResponseEntity.ok(entryService.createNewDevolution(saleId, userId));
        }

        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/newEntry", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity createNewSale (@RequestParam("userId") Integer userId){

        try{
            return ResponseEntity.ok(entryService.createNewEntry(userId, "entry"));
        }

        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity create (@RequestBody EntryDto entryDto){

        try{
            return ResponseEntity.ok(entryService.createEntry(entryDto));
        }

        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity update (@PathVariable("id") Integer id, @RequestBody EntryDto entryDto){

        try{
            entryService.updateEntry(entryDto, id);
            return ResponseEntity.ok(HttpStatus.OK);
        }

        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity delete (@PathVariable Integer id){

        try{
            entryService.deleteEntry(id);
            return ResponseEntity.ok(HttpStatus.OK);
        }

        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/confirm/{id}")
    @ResponseBody
    public ResponseEntity confirmAndSumStock (@PathVariable Integer id){

        try{
            return ResponseEntity.ok(entryService.confirmAndSumStock(id));
        }

        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/createDevolution", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity createDevolution(@RequestParam("saleId") Integer saleId, @RequestParam("userId") Integer userId){
        try{
            Sale sale = saleDao.findSaleById(saleId);
            if(sale == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("La venta ingresada no existe en los registros.");
            }else{
                return ResponseEntity.ok(entryService.createDevolution(sale, userId));
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}