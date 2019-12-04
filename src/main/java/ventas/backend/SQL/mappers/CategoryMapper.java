package ventas.backend.SQL.mappers;

import ventas.backend.SQL.dto.CategoryDto;
import ventas.backend.SQL.models.Category;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryMapper {

    public List<CategoryDto> mapToDtoArrayList(List<Category> categoryArrayList) {
        int i;
        List<CategoryDto> categoryDtoArrayList = new ArrayList<>();
        for(i=0;i<categoryArrayList.size();i++){
            categoryDtoArrayList.add(mapToDto(categoryArrayList.get(i)));
        }
        return categoryDtoArrayList;
    }

    public Category mapToModel(CategoryDto categoryDto){
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        category.setDeleted(categoryDto.getDeleted());
        category.setSubCategoryList(categoryDto.getSubCategoryList());
        return category;
    }

    public CategoryDto mapToDto (Category category){
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setDeleted(category.getDeleted());
        categoryDto.setSubCategoryList(category.getSubCategoryList());
        return categoryDto;
    }
}