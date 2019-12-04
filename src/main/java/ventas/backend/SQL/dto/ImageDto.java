package ventas.backend.SQL.dto;

import ventas.backend.SQL.models.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties({"product", "id"})
public class ImageDto {
    private Integer id;
    private String name;
    private String extension;
    private Boolean principal;
    private Product product;
}
