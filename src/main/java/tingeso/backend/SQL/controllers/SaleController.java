package tingeso.backend.SQL.controllers;

import tingeso.backend.SQL.dao.SaleDao;
import tingeso.backend.SQL.dto.SaleDto;
import tingeso.backend.SQL.models.Sale;
import tingeso.backend.SQL.services.BarCodeService;
import tingeso.backend.SQL.services.ReducedSaleDetailService;
import tingeso.backend.SQL.services.SalePaymentMethodService;
import tingeso.backend.SQL.services.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Logger;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/sales")
public class SaleController {
    @Autowired
    private SaleService saleService;

    @Autowired
    private SalePaymentMethodService salePaymentMethodService;

    @Autowired
    private SaleDao saleDao;

    @Autowired
    private BarCodeService barCodeService;

    @Autowired
    private ReducedSaleDetailService reducedSaleDetailService;

    Logger logger = Logger.getLogger(SaleController.class.getName());
    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<List<SaleDto>> getAllSale(){
        try{
            return ResponseEntity.ok(saleService.getAllSale());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<SaleDto> findSaleById (@PathVariable("id") Integer id){

        try{
            return ResponseEntity.ok(saleService.getSaleById(id));
        }

        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/currentsSales")
    @ResponseBody
    public ResponseEntity<List<SaleDto>> getCurrentsSales(){
        try{
            return ResponseEntity.ok(saleService.getCurrentsSales());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/finalized")
    @ResponseBody
    public ResponseEntity<List<SaleDto>> getFinalizedSales(){
        try{
            return ResponseEntity.ok(saleService.getFinalizedSales());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity create (@RequestBody SaleDto saleDto){

        try{
            return ResponseEntity.ok(saleService.createSale(saleDto));
        }

        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @RequestMapping(value = "/newSale", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity createNewSale (@RequestParam("id") Integer userId,
                                         @RequestParam("deskId") Integer deskId){

        try{
            return ResponseEntity.ok(saleService.createSaleNewSale(userId, deskId));
        }

        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/cancelSale")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity cancelSale (@RequestParam("id") Integer id){

        try{
            return ResponseEntity.ok(saleService.cancelSale(id));
        }

        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/finalizeSale")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity finalizeSale (@RequestParam("id") Integer id){

        try{
            Sale sale = saleDao.findSaleById(id);
            if(sale.getSalePaymentMethodList().size() == 0){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("No se ha regristrado ningún pago");
            }
            if(salePaymentMethodService.getActualyPaymentAmountForSale(sale).compareTo(sale.getTotalWithDiscount().subtract(sale.getPositiveBalance())) < 0){
                logger.info("No es posible finalizar la venta si el pago no ha sido efectuado en totalidad");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("No es posible finalizar la venta si el pago no ha sido efectuado en totalidad");

            }
            return ResponseEntity.ok(reducedSaleDetailService.finalizeSale(id));
        }

        catch (Exception e){
            System.out.println("Error: " + e);
            logger.info(e.getCause().toString());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity update (@PathVariable("id") Integer id, @RequestBody SaleDto saleDto){

        try{
            return ResponseEntity.ok(saleService.updateSale(saleDto, id));
        }

        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity delete (@PathVariable Integer id){

        try{
            saleService.deleteSale(id);
            return ResponseEntity.ok(HttpStatus.OK);
        }

        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/cleanSaleDetails/{id}")
    @ResponseBody
    public ResponseEntity cleanSaleDetails(@PathVariable("id") Integer id){
        try {
            return ResponseEntity.ok(saleService.cleanSaleDetails(id));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @RequestMapping(value = "barcode/{id}", method = RequestMethod.GET)
    public void barcode(@PathVariable("id") String id, HttpServletResponse response) throws Exception {
        response.setContentType("image/png");
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(barCodeService.getBarCodeImage(id, 400, 200));
        outputStream.flush();
        outputStream.close();
    }

}