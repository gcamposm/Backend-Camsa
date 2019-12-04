package tingeso.backend.SQL.dto;

import tingeso.backend.SQL.models.SubCategory;
import lombok.Data;
import java.util.List;

@Data
public class CategoryDto {
    private Integer id;
    private String name;
    private Boolean deleted;
    private List<SubCategory> subCategoryList;
}