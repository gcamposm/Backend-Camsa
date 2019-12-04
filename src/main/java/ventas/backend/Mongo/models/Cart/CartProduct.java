package ventas.backend.Mongo.models.Cart;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartProduct {
    private Integer id;
    private String code;
    private String name;
    private BigDecimal price;
    private Integer discount;
}