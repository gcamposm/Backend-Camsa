package ventas.backend.SQL.controllers;

import ventas.backend.SQL.dto.SubCategoryDto;
import ventas.backend.SQL.services.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/subCategories")
public class SubCategoryController {

    @Autowired
    private SubCategoryService subCategoryService;

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<List<SubCategoryDto>> getAllCategory(){
        try{
            return ResponseEntity.ok(subCategoryService.getAllSubCategory());
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<SubCategoryDto> findCategoryById (@PathVariable("id") Integer id){
        try{
            return ResponseEntity.ok(subCategoryService.getSubCategoryById(id));
        } catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("category/{id}")
    @ResponseBody
    public ResponseEntity getSubCategoryByCategoryId (@PathVariable("id") Integer id){
        try{
            return ResponseEntity.ok(subCategoryService.getSubCategoryByCategoryId(id));
        } catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("web/category/{id}")
    @ResponseBody
    public ResponseEntity getSubCategoryByCategoryIdForWeb(@PathVariable("id") Integer id){
        try{
            return ResponseEntity.ok(subCategoryService.getSubCategoryByCategoryId(id));
        } catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity create (@RequestParam("name") String name, @RequestParam("categoryId") Integer categoryId){
        try{
            return ResponseEntity.ok(subCategoryService.createSubCategory(name, categoryId));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity update (@PathVariable("id") Integer id, @RequestBody SubCategoryDto subCategoryDto){
        try{
            subCategoryService.updateSubCategory(subCategoryDto, id);
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
            subCategoryService.deleteSubCategory(id);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
