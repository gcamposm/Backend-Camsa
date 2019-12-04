package tingeso.backend.SQL.services;


import tingeso.backend.SQL.dao.CategoryDao;
import tingeso.backend.SQL.dto.CategoryDto;
import tingeso.backend.SQL.mappers.CategoryMapper;
import tingeso.backend.SQL.models.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private CategoryMapper categoryMapper;

    public List<CategoryDto> getAllCategory(){
        List<Category> categoryList = categoryDao.findCategoriesByDeletedFalse();
        return categoryMapper.mapToDtoArrayList(categoryList);
    }

    public CategoryDto getCategoryById(Integer id){
        if(categoryDao.findById(id).isPresent()){
            return categoryMapper.mapToDto(categoryDao.findCategoryById(id));
        }
        else{
            return  null;
        }
    }

    public void updateCategory(CategoryDto categoryDto, Integer id){
        if(categoryDao.findById(id).isPresent()){
            Category categoryFinded = categoryDao.findCategoryById(id);
            categoryFinded.setName(categoryDto.getName());
            categoryFinded.setDeleted(categoryDto.getDeleted());
            categoryFinded.setSubCategoryList(categoryDto.getSubCategoryList());
            categoryDao.save(categoryFinded);
        }
    }

    public CategoryDto deleteCategory(Integer id){
        Category category = categoryDao.findCategoryById(id);
        category.setDeleted(true);
        return categoryMapper.mapToDto(categoryDao.save(category));
    }

    public CategoryDto createCategory(CategoryDto categoryDto){
        return categoryMapper.mapToDto(categoryDao.save(categoryMapper.mapToModel(categoryDto)));
    }
}