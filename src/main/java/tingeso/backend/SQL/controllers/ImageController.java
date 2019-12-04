package tingeso.backend.SQL.controllers;

import tingeso.backend.SQL.dao.ProductDao;
import tingeso.backend.SQL.dto.ImageDto;
import tingeso.backend.SQL.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/images")
public class ImageController {
    @Autowired
    private ImageService imageService;

    @Autowired
    private ProductDao productDao;

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<List<ImageDto>> getAllImage(){
        try{
            return ResponseEntity.ok(imageService.getAllImages());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ImageDto> findImageById (@PathVariable("id") Integer id){

        try{
            return ResponseEntity.ok(imageService.getImageById(id));
        }

        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity create (@RequestBody ImageDto imageDto){

        try{
            return ResponseEntity.ok(imageService.createImage(imageDto));
        }

        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity update (@PathVariable("id") Integer id, @RequestBody ImageDto imageDto){

        try{
            imageService.updateImage(imageDto, id);
            return ResponseEntity.ok(HttpStatus.OK);
        }

        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }

    @DeleteMapping("/delete/{name}")
    @ResponseBody
    public ResponseEntity<String> delete (@PathVariable String name){

        try{
            return ResponseEntity.ok(imageService.deleteImage(name));
        }

        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("/uploadMultipleImages")
    @ResponseBody
    public ResponseEntity uploadMultipleImages (@RequestBody String[] imageNames){

        try{
            return ResponseEntity.ok(imageService.uploadMultipleImages(imageNames));
        }

        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

}
