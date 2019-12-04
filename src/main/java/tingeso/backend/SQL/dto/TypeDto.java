package tingeso.backend.SQL.dto;

import tingeso.backend.SQL.models.Product;
import lombok.Data;

import java.util.List;

@Data
public class TypeDto {
    private Integer id;
    private String type;

    // RELATIONS

    // type -> product
    private List<Product> productList;
}
