package tingeso.backend.SQL.dto;

import tingeso.backend.SQL.models.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class SubCategoryDto {
    private Integer id;
    private String name;
    private Boolean deleted;
    @JsonIgnore
    private Category category;
}