package tingeso.backend.SQL.controllers;


import tingeso.backend.SQL.dto.TypeDto;
import tingeso.backend.SQL.services.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/types")
public class TypeController {
    @Autowired
    private TypeService typeService;

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<List<TypeDto>> getAllType(){
        try{
            return ResponseEntity.ok(typeService.getAllType());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<TypeDto> findTypeById (@PathVariable("id") Integer id){

        try{
            return ResponseEntity.ok(typeService.getTypeByID(id));
        }

        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity create (@RequestBody TypeDto typeDto){

        try{
            return ResponseEntity.ok(typeService.createType(typeDto));
        }

        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity update (@PathVariable("id") Integer id, @RequestBody TypeDto typeDto){

        try{
            typeService.updateType(typeDto, id);
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
            typeService.deleteType(id);
            return ResponseEntity.ok(HttpStatus.OK);
        }

        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
