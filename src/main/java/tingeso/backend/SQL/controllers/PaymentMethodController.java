package tingeso.backend.SQL.controllers;

import tingeso.backend.SQL.dto.PaymentMethodDto;
import tingeso.backend.SQL.services.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/paymentmethods")
public class PaymentMethodController {

    @Autowired
    private PaymentMethodService paymentMethodService;

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<List<PaymentMethodDto>> getAllPaymentMethods(){
        try{
            return ResponseEntity.ok(paymentMethodService.getAllPaymentMethods());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<PaymentMethodDto> findImageById (@PathVariable("id") Integer id){

        try{
            return ResponseEntity.ok(paymentMethodService.getPaymentMethodById(id));
        }

        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity create (@RequestBody PaymentMethodDto paymentMethodDto){
        try{
            return ResponseEntity.ok(paymentMethodService.createPaymentMethod(paymentMethodDto));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity update (@PathVariable("id") Integer id, @RequestBody PaymentMethodDto paymentMethodDto){
        try{
            paymentMethodService.updatePaymentMethod(paymentMethodDto, id);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity delete (@PathVariable Integer id){
        try{
            paymentMethodService.deletePaymentMethod(id);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}