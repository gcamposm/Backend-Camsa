package ventas.backend.SQL.mappers;

import ventas.backend.SQL.dto.SubCategoryDto;
import ventas.backend.SQL.models.SubCategory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
@Component
public class SubCategoryMapper {

    public SubCategory mapToModel(SubCategoryDto subCategoryDto){
        SubCategory subCategory = new SubCategory();
        subCategory.setId(subCategoryDto.getId());
        subCategory.setName(subCategoryDto.getName());
        subCategory.setDeleted(subCategoryDto.getDeleted());
        subCategory.setCategory(subCategoryDto.getCategory());
        return subCategory;
    }

    public SubCategoryDto mapToDto(SubCategory subCategory){
        SubCategoryDto subCategoryDto = new SubCategoryDto();
        subCategoryDto.setId(subCategory.getId());
        subCategoryDto.setName(subCategory.getName());
        subCategoryDto.setDeleted(subCategory.getDeleted());
        subCategoryDto.setCategory(subCategory.getCategory());
        return subCategoryDto;
    }

    public List<SubCategoryDto> mapToDtoArrayList(List<SubCategory> subCategoryList) {
        int i;
        List<SubCategoryDto> categoryDtoArrayList = new ArrayList<>();
        for(i=0;i<subCategoryList.size();i++){
            categoryDtoArrayList.add(mapToDto(subCategoryList.get(i)));
        }
        return categoryDtoArrayList;
    }
}