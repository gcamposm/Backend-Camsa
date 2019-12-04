package tingeso.backend.SQL.controllers;


import tingeso.backend.SQL.dto.CategoryDto;
import tingeso.backend.SQL.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<List<CategoryDto>> getAllCategories(){
        try{
            return ResponseEntity.ok(categoryService.getAllCategory());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/web/")
    @ResponseBody
    public ResponseEntity<List<CategoryDto>> getAllCategoriesForWeb(){
        try{
            return ResponseEntity.ok(categoryService.getAllCategory());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<CategoryDto> findCategoryById (@PathVariable("id") Integer id){

        try{
            return ResponseEntity.ok(categoryService.getCategoryById(id));
        }

        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity create (@RequestBody CategoryDto categoryDto){

        try{
            return ResponseEntity.ok(categoryService.createCategory(categoryDto));
        }

        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity update (@PathVariable("id") Integer id, @RequestBody CategoryDto categoryDto){

        try{
            categoryService.updateCategory(categoryDto, id);
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
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(HttpStatus.OK);
        }

        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}