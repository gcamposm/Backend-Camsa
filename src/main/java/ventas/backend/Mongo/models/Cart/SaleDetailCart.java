package ventas.backend.Mongo.models.Cart;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@SuppressWarnings("Duplicates")
@Data
@NoArgsConstructor
public class SaleDetailCart {
    private Integer quantity;
    private BigDecimal discount;
    private BigDecimal subtotal;
    private BigDecimal totalWithDiscount;
    // RELATIONS
    // product -> saleDetail
    private CartProduct product;

    // sale -> saleDetail
    //Calcular subtotal dependiendo del tipo de precio
    public void calculateSubTotal(){
        BigDecimal bigQuantity = BigDecimal.valueOf(this.quantity);
        this.subtotal = bigQuantity.multiply(this.product.getPrice());
    }
    //Calcular el descuento y el subtotal con descuento
    public void calculateDiscount(){
        int productDiscount = product.getDiscount();
        double discountFactor = productDiscount/100.0;
        BigDecimal bigDiscountFactor = BigDecimal.valueOf(discountFactor);
        this.discount = BigDecimal.valueOf(subtotal.multiply(bigDiscountFactor).intValue());
        this.totalWithDiscount = this.subtotal.subtract(this.discount);
    }
}
