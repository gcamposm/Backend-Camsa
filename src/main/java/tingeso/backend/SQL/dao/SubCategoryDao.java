package tingeso.backend.SQL.dao;

import tingeso.backend.SQL.models.Category;
import tingeso.backend.SQL.models.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubCategoryDao extends JpaRepository<SubCategory, Integer> {
    SubCategory findSubCategoriesById(Integer id);
    SubCategory findSubCategoriesByName(String name);
    List<SubCategory> findSubCategoriesByCategoryAndDeleted(Category category, Boolean deleted);
    List<SubCategory> findSubCategoriesByDeletedFalse();
}
