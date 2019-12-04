package tingeso.backend.SQL.controllers;

import tingeso.backend.SQL.dao.SaleDao;
import tingeso.backend.SQL.dao.SaleDetailDao;
import tingeso.backend.SQL.dto.ProductInterfaceDto;
import tingeso.backend.SQL.dto.SaleDetailDto;
import tingeso.backend.SQL.dto.SaleInformationDto;
import tingeso.backend.SQL.mappers.SaleMapper;
import tingeso.backend.SQL.models.Product;
import tingeso.backend.SQL.models.Sale;
import tingeso.backend.SQL.services.ProductService;
import tingeso.backend.SQL.services.ReducedSaleDetailService;
import tingeso.backend.SQL.services.SaleDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@SuppressWarnings("Duplicates")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/saledetails")
public class SaleDetailController {
    @Autowired
    private SaleDetailService saleDetailService;

    @Autowired
    private ProductService productService;

    //CAMBIAR POR SERVICE LUEGO
    @Autowired
    private SaleDetailDao saleDetailDao;

    @Autowired
    private SaleDao saleDao;

    @Autowired
    private SaleMapper saleMapper;

    @Autowired
    private ReducedSaleDetailService reducedSaleDetailService;

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<List<SaleDetailDto>> getAllSaleDetails(){
        try{
            return ResponseEntity.ok(saleDetailService.getAllSaleDetail());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<SaleDetailDto> findSaleDetailById (@PathVariable("id") Integer id){

        try{
            return ResponseEntity.ok(saleDetailService.getSaleDetailById(id));
        }

        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity create (@RequestBody SaleDetailDto saleDetailDto){

        try{
            return ResponseEntity.ok(saleDetailService.createSaleDetail(saleDetailDto));
        }

        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity update (@PathVariable("id") Integer id, @RequestBody SaleDetailDto saleDetailDto){

        try{
            saleDetailService.updateSaleDetail(saleDetailDto, id);
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
            saleDetailService.deleteSaleDetail(id);
            return ResponseEntity.ok(HttpStatus.OK);
        }

        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @RequestMapping(value = "/appendProduct", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity createSaleDetailWithProduct (@RequestParam("saleId") Integer saleId ,
                                                       @RequestParam("productId") Integer productId){

        try{
            ProductInterfaceDto productDto = productService.getProductByID(productId);
            if(productDto.getStock() <= 0){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("No hay stock suficiente");
            }
            saleDetailService.createSaleDetailWithProduct(saleId, productId);
            Sale sale = findAndActualizeSale(saleId);
            return ResponseEntity.ok(sale);
        }

        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @RequestMapping(value = "/editSaleDetail", method = RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity changeUnitaryPrice (@RequestParam("saleDetailId") int saleDetailId ,
                                              @RequestParam("unitaryPrice") int unitaryPrice){
        try{
            return ResponseEntity.ok(saleDetailService.changeUnitaryPrice(saleDetailId, unitaryPrice));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }



    @RequestMapping(value = "/appendProducts", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity appendProduct  (@RequestParam("saleId") Integer saleId ,
                                          @RequestParam("productId") Integer productId,
                                          @RequestParam("quantity") Integer quantity){

        try{
            if(quantity < 0){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cantidad ingresada no válida");
            }
            Product product = productService.getRealProductById(productId);
            if(product.getStock() <= 0){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("No hay stock suficiente");
            }
            if(product.getStock() < quantity){
                return ResponseEntity.ok(saleDetailService.appendProductWithQuantity(
                        saleId, product, product.getStock()));
            }
            else{
                return ResponseEntity.ok(saleDetailService.appendProductWithQuantity(
                        saleId, product, quantity));
            }
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }
    @RequestMapping(value = "/appendProductsCode", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity appendProductByCode(@RequestParam("saleId") Integer saleId ,
                                              @RequestParam("productCode") String productCode,
                                              @RequestParam("quantity") Integer quantity){
        try{
            if(quantity < 0){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cantidad ingresada no válida");
            }
            Product product = productService.getProductByCode(productCode);
            if(product.getStock() <= 0){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("No hay stock suficiente");
            }
            if(product.getStock() < quantity){
                return ResponseEntity.ok(saleDetailService.appendProductWithQuantity(
                        saleId, product, product.getStock()));
            }
            else{
                return ResponseEntity.ok(saleDetailService.appendProductWithQuantity(
                        saleId, product, quantity));
            }
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error: " + e);
        }

    }
    @RequestMapping(value = "/appendJokerProducts", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity appendJokerProducts  (@RequestParam("saleId") Integer saleId,
                                                @RequestParam("productId") Integer productId,
                                                @RequestParam("quantity") Integer quantity,
                                                @RequestParam("price") Integer price,
                                                @RequestParam("productName") String productName){
        try{
           return ResponseEntity.ok(saleDetailService.appendJokerProducts(saleId, productId, quantity, price, productName));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }



    private Sale findAndActualizeSale(Integer saleId){
        Sale sale = saleDao.findSaleById(saleId);
        BigDecimal total = saleDetailDao.sumAllProductsBySaleId(sale);
        Integer totalQuantity = saleDetailDao.sumAllQuantity(sale);
        sale.setTotalDiscount(saleDetailDao.sumAllDiscount(sale));
        sale.setTotalWithDiscount(saleDetailDao.sumAllTotalsWithDiscounts(sale).subtract(sale.getPositiveBalance()));
        sale.setTotal(total);
        sale.setTotalQuantity(totalQuantity);
        saleDao.save(sale);
        return sale;
    }



    @RequestMapping(value = "/quitProduct", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity quitProduct(@RequestParam("saleId") Integer saleId , @RequestParam("productId") Integer productId){
        try{
            return ResponseEntity.ok(saleDetailService.quitProduct(saleId,productId));
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @RequestMapping(value = "/editQuantity", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity editQuantity(@RequestParam("saleId") Integer saleId,
                                       @RequestParam("productId") Integer productId,
                                       @RequestParam("quantity") Integer quantity){
        try{
            return ResponseEntity.ok(saleDetailService.editQuantity(saleId, productId, quantity));
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @RequestMapping(value = "/addDiscount", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity addDiscount(@RequestParam("saleId") Integer saleId , @RequestParam("productId") Integer productId,@RequestParam("discount") Integer discount){
        try{
            return ResponseEntity.ok(saleDetailService.addDiscount(saleId, productId, discount));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getCause());
        }
    }
    @GetMapping("/sale/{id}")
    @ResponseBody
    public ResponseEntity findSaleDetailsBySaleId(@PathVariable Integer id){
        try{
            return ResponseEntity.ok(saleDetailService.findSaleDetailBySaleId(id));
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    // Metodos para venta desde frontend
    @PostMapping("/createSaleWithSaleDetails")
    @ResponseBody
    public ResponseEntity createSaleWithSaleDetails(@RequestBody SaleInformationDto saleInformationDto){
        try{
            System.out.println(saleInformationDto.toString());
            return ResponseEntity.ok(reducedSaleDetailService.createSaleWithSaleDetails(saleInformationDto));
        }catch (Exception e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
