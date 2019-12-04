package ventas.backend.SQL.controllers;

import ventas.backend.SQL.dao.EntryDao;
import ventas.backend.SQL.dto.EntryDetailDto;
import ventas.backend.SQL.dto.ProductInterfaceDto;
import ventas.backend.SQL.models.Entry;
import ventas.backend.SQL.services.EntryDetailService;
import ventas.backend.SQL.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/entrydetails")
public class EntryDetailController {

    @Autowired
    private EntryDao entryDao;

    @Autowired
    private EntryDetailService entryDetailService;

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<List<EntryDetailDto>> getAllEntryDetail(){
        try{
            return ResponseEntity.ok(entryDetailService.getAllEntryDetails());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<EntryDetailDto> findentryDetailById (@PathVariable("id") Integer id){

        try{
            return ResponseEntity.ok(entryDetailService.getEntryDetailById(id));
        }

        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity create (@RequestBody EntryDetailDto entryDetailDto){

        try{
            return ResponseEntity.ok(entryDetailService.createEntryDetail(entryDetailDto));
        }

        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity update (@PathVariable("id") Integer id, @RequestBody EntryDetailDto entryDetailDto){

        try{
            entryDetailService.updateEntryDetail(entryDetailDto, id);
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
            entryDetailService.deleteEntryDetail(id);
            return ResponseEntity.ok(HttpStatus.OK);
        }

        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Este detalle de entrada no existe");
        }
    }

    @RequestMapping(value = "/appendProduct", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity createEntryDetailWithProduct (@RequestParam("entryId") Integer entryId , @RequestParam("productId") Integer productId){
        try{
            ProductInterfaceDto productDto = productService.getProductByID(productId);
            if(productDto == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El producto solicitado no existe.");
            }else{
                Entry entry = entryDao.findEntryById(entryId);
                if(entry == null)
                {
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body("La entrada asociada no existe.");
                }else{
                    return ResponseEntity.ok(entryDetailService.createEntryDetailWithProduct(productDto, entry));
                }
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/quitProduct", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity quitProduct(@RequestParam("entryId") Integer entryId , @RequestParam("productId") Integer productId){
        try{
            ProductInterfaceDto productDto = productService.getProductByID(productId);
            if(productDto == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El producto solicitado no existe.");
            }else{
                Entry entry = entryDao.findEntryById(entryId);
                if(entry == null)
                {
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body("La entrada asociada no existe.");
                }else{
                    return ResponseEntity.ok(entryDetailService.quitProduct(productDto, entry));
                }
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/editQuantity", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity editQuantity(@RequestParam("entryId") Integer entryId , @RequestParam("productId") Integer productId,@RequestParam("quantity") Integer quantity){
        try{
            ProductInterfaceDto productDto = productService.getProductByID(productId);
            if(productDto == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El producto solicitado no existe.");
            }else{
                Entry entry = entryDao.findEntryById(entryId);
                if(entry == null)
                {
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body("La entrada asociada no existe.");
                }else{
                    if(quantity<0)
                    {
                        return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body("No puede ingresar una cantidad negativa.");
                    }else
                    {
                        return ResponseEntity.ok(entryDetailService.editQuantity(productDto, entry, quantity));
                    }
                }
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/entry/{id}")
    @ResponseBody
    public ResponseEntity getEntryDetails(@PathVariable Integer id){
        try{
            Entry entry = entryDao.findEntryById(id);
            if(entry == null)
            {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("La entrada asociada no existe.");
            }else{
                return ResponseEntity.ok(entryDetailService.getEntryDetails(entry));
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @RequestMapping(value = "/devolutionProduct", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity appendNewDevolution (@RequestParam("entryId") Integer entryId ,
                                               @RequestParam("saleDetailId") Integer saleDetailId,
                                               @RequestParam("quantity") Integer quantity,
                                               @RequestParam("type") String type,
                                               @RequestParam("stock") Boolean stock){
        try{
            return ResponseEntity.ok(entryDetailService.appendEntryDetailForDevolution(entryId, saleDetailId, quantity, type, stock));
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}