package ventas.backend.SQL.controllers;

import ventas.backend.SQL.dto.ProviderDto;
import ventas.backend.SQL.services.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/providers")
public class ProviderController {
    @Autowired
    private ProviderService providerService;

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<List<ProviderDto>> getAllProvider(){
        try{
            return ResponseEntity.ok(providerService.getAllProvider());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ProviderDto> findProviderById (@PathVariable("id") Integer id){

        try{
            return ResponseEntity.ok(providerService.getProviderByID(id));
        }

        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity create (@RequestBody ProviderDto providerDto){

        try{
            return ResponseEntity.ok(providerService.createProvider(providerDto));
        }

        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity update (@PathVariable("id") Integer id, @RequestBody ProviderDto providerDto){

        try{
            return ResponseEntity.ok(providerService.updateProvider(providerDto, id));
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
            providerService.deleteProvider(id);
            return ResponseEntity.ok(HttpStatus.OK);
        }

        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
