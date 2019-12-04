package ventas.backend.SQL.services;

import ventas.backend.SQL.dao.CategoryDao;
import ventas.backend.SQL.dao.SubCategoryDao;
import ventas.backend.SQL.dto.SubCategoryDto;
import ventas.backend.SQL.mappers.SubCategoryMapper;
import ventas.backend.SQL.models.Category;
import ventas.backend.SQL.models.SubCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SubCategoryService {

    @Autowired
    private SubCategoryDao subCategoryDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private SubCategoryMapper subCategoryMapper;

    public List<SubCategoryDto> getAllSubCategory(){
        List<SubCategory> subCategoryList = subCategoryDao.findSubCategoriesByDeletedFalse();
        return subCategoryMapper.mapToDtoArrayList(subCategoryList);
    }

    public SubCategoryDto getSubCategoryById(Integer id){
        if(subCategoryDao.findById(id).isPresent()){
            return subCategoryMapper.mapToDto(subCategoryDao.findSubCategoriesById(id));
        }
        else{
            return  null;
        }
    }

    public void updateSubCategory(SubCategoryDto subCategoryDto, Integer id){
        if(subCategoryDao.findById(id).isPresent()){
            SubCategory subCategoryFinded = subCategoryDao.findSubCategoriesById(id);
            subCategoryFinded.setName(subCategoryDto.getName());
            subCategoryFinded.setDeleted(subCategoryDto.getDeleted());
            subCategoryFinded.setCategory(subCategoryDto.getCategory());
            subCategoryDao.save(subCategoryFinded);
        }
    }

    public SubCategoryDto createSubCategory(String name, Integer categoryId){
        SubCategoryDto subCategoryDto = new SubCategoryDto();
        subCategoryDto.setCategory(categoryDao.findCategoryById(categoryId));
        subCategoryDto.setName(name);
        subCategoryDto.setDeleted(false);
        return subCategoryMapper.mapToDto(subCategoryDao.save(subCategoryMapper.mapToModel(subCategoryDto)));
    }

    public List<SubCategoryDto> getSubCategoryByCategoryId(Integer categoryId){
        Category category = categoryDao.findCategoryById(categoryId);
        return subCategoryMapper.mapToDtoArrayList(subCategoryDao.findSubCategoriesByCategoryAndDeleted(category, false));
    }

    public SubCategoryDto deleteSubCategory(Integer id){
        SubCategory subCategory = subCategoryDao.findSubCategoriesById(id);
        subCategory.setDeleted(true);
        subCategoryDao.save(subCategory);
        return subCategoryMapper.mapToDto(subCategory);
    }
}