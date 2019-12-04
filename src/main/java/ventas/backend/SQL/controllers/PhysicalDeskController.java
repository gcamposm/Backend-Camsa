package ventas.backend.SQL.controllers;

import ventas.backend.SQL.dto.PhysicalDeskDto;
import ventas.backend.SQL.services.PhysicalDeskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/physicaldesks")
public class PhysicalDeskController {
    @Autowired
    private PhysicalDeskService physicalDeskService;

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity getAllPaymentMethods(){
        try{
            return ResponseEntity.ok(physicalDeskService.getAllPhysicalDesk());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity findImageById (@PathVariable("id") Integer id){

        try{
            return ResponseEntity.ok(physicalDeskService.getPhysicalDeskById(id));
        }

        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity create (@RequestBody PhysicalDeskDto physicalDeskDto){
        try{
            return ResponseEntity.ok(physicalDeskService.createPhysicalDesk(physicalDeskDto));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity update (@PathVariable("id") Integer id, @RequestBody PhysicalDeskDto physicalDeskDto){
        try{
            physicalDeskService.updatePhysicalDesk(physicalDeskDto, id);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity delete (@PathVariable Integer id){
        try{
            physicalDeskService.deletePhysicalDesk(id);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(value = "/newPhysicalDesk", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity createNewPhysicalDesk(@RequestParam("office") String office,
                                                @RequestParam("description") String description){
        try{
            return ResponseEntity.ok(physicalDeskService.createNewPhysicalDesk(office, description));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}