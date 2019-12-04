package tingeso.backend.SQL.dto;

import tingeso.backend.SQL.models.Product;
import lombok.Data;

@Data
public class ProductQuantity {
    private Product product;
    private Integer localQuantity;
    private Integer warehouseQuantity;
    private Integer eventQuantity;

    public ProductQuantity(Product product, Integer localQuantity, Integer warehouseQuantity, Integer eventQuantity) {
        this.product = product;
        this.localQuantity = localQuantity;
        this.warehouseQuantity = warehouseQuantity;
        this.eventQuantity = eventQuantity;
    }

    @Override
    public String toString() {
        return "ProductQuantity{" +
                "product=" + product.getId() +
                ", localQuantity=" + localQuantity +
                ", warehouseQuantity=" + warehouseQuantity +
                ", eventQuantity=" + eventQuantity +
                '}';
    }
}
