package tingeso.backend.SQL.controllers;

import tingeso.backend.Email.EmailSenderPaymentAmounth;
import tingeso.backend.SQL.dao.DeskDao;
import tingeso.backend.SQL.dto.SalePaymentMethodDto;
import tingeso.backend.SQL.dto.SaleWithPaymentMethodEncapsulatedDto;
import tingeso.backend.SQL.services.SalePaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/salepaymentmethod")
public class SalePaymentMethodController {

    @Autowired
    private SalePaymentMethodService salePaymentMethodService;

    @Autowired private DeskDao deskDao;

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<List<SalePaymentMethodDto>> getAllSalePaymentMethod(){
        try{
            return ResponseEntity.ok(salePaymentMethodService.getAllSalePaymentMethod());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<SalePaymentMethodDto> findSalePaymentMethodById (@PathVariable("id") Integer id){

        try{
            return ResponseEntity.ok(salePaymentMethodService.getSalePaymentMethodById(id));
        }

        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity create (@RequestBody SalePaymentMethodDto salePaymentMethodDto){

        try{
            return ResponseEntity.ok(salePaymentMethodService.createSalePaymentMethod(salePaymentMethodDto));
        }

        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity update (@PathVariable("id") Integer id, @RequestBody SalePaymentMethodDto salePaymentMethodDto){

        try{
            salePaymentMethodService.updateSalePaymentMethod(salePaymentMethodDto, id);
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
            salePaymentMethodService.deleteSalePaymentMethod(id);
            return ResponseEntity.ok(HttpStatus.OK);
        }

        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/salewithpaymentmethod", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity salewithpaymentmethod (@RequestParam("saleId") Integer saleId,
                                                 @RequestParam("paymentMethodId") Integer paymentMethodId,
                                                 @RequestParam("amount") Integer amount,
                                                 @RequestParam("exchange") Integer exchange){

        try{
            return ResponseEntity.ok(salePaymentMethodService.createSalePaymentMethodWithSale(saleId, paymentMethodId, amount, exchange));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getSalePaymentMethodForSale/{id}")
    public ResponseEntity getSalePaymentMethodForSale(@PathVariable("id") int id){
        try{
            return ResponseEntity.ok(salePaymentMethodService.getSalePaymentMethodForSale(id));
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getCause());
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/getPaymentMethodForAllSalesInADesk/{deskId}")
    public ResponseEntity getPaymentMethodForAllSalesInADesk(@PathVariable("deskId") int deskId){
        try{
            return ResponseEntity.ok(salePaymentMethodService.getPaymentMethodForAllSalesInADesk(deskId));
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getCause());
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/getDetailedDeskStatistics/{deskId}")
    public ResponseEntity getDetailedDeskStatistics(@PathVariable("deskId") int deskId){
        try{
            List<SaleWithPaymentMethodEncapsulatedDto> saleWithPaymentMethodEncapsulatedDtoList =
                    salePaymentMethodService.getPaymentMethodForAllSalesInADesk(deskId);
            EmailSenderPaymentAmounth emailSenderPaymentAmounth = new EmailSenderPaymentAmounth();
            emailSenderPaymentAmounth.setSaleWithPaymentMethodsDtos(saleWithPaymentMethodEncapsulatedDtoList);
            emailSenderPaymentAmounth.setDesk(deskDao.findDeskById(deskId));
            return ResponseEntity.ok(emailSenderPaymentAmounth.createReport());
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getCause());
        }
    }

}
