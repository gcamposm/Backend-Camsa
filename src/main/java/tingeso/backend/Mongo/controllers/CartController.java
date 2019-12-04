package tingeso.backend.Mongo.controllers;


import tingeso.backend.Mongo.models.Cart.Cart;
import tingeso.backend.Mongo.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/web/")
    @ResponseBody
    public ResponseEntity<List<Cart>> findAllCarts (){
        try{
            return ResponseEntity.ok(cartService.findAll());
        } catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/web/{id}")
    @ResponseBody
    public ResponseEntity<Cart> findCartById (@PathVariable("id") String id){
        try{
            return ResponseEntity.ok(cartService.getCart(id));
        } catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/web/add", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<Cart> addProduct(@RequestParam("cartId") String cartId , @RequestParam("productId") Integer productId,@RequestParam("quantity") Integer quantity){
        try{
            return ResponseEntity.ok(cartService.addProduct(cartId, productId, quantity));
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/web/remove", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<Cart> removeProduct(@RequestParam("cartId") String cartId , @RequestParam("productId") Integer productId){
        try{
            return ResponseEntity.ok(cartService.removeProduct(cartId, productId));
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/web/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<Cart> creteNewCart(){
        try{
            return ResponseEntity.ok(cartService.createCart());
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping(value = "/createSale", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity createSale(@RequestParam("cartId") String cartId){
        try{
            return ResponseEntity.ok(cartService.createSale(cartId));
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}