package tingeso.backend.Mongo.models.Cart;

import tingeso.backend.SQL.models.Product;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "shopping_cart")
@Data
public class Cart {
    @Id
    private String id;
    private List<SaleDetailCart> saleDetailCartList;
    private BigDecimal total;
    private BigDecimal totalWithDiscount;
    private BigDecimal discount;
    private String type;
    private LocalDateTime date;

    public Cart(){
        this.saleDetailCartList = new ArrayList<>();
        type = "shoppingCart";
        date = LocalDateTime.now();
    }

    public Cart addProduct(Product product, Integer quantity){
        CartProduct cartProduct = mappToCartProduct(product);
        SaleDetailCart saleDetailCartObtained = searchSailDetailByProduct(cartProduct);
        if(saleDetailCartObtained == null) {
            SaleDetailCart saleDetailCart = new SaleDetailCart();
            saleDetailCart.setProduct(cartProduct);
            saleDetailCart.setQuantity(quantity);
            //Cambiar luego al tipo de precio web
            saleDetailCart.calculateSubTotal();
            saleDetailCart.calculateDiscount();
            List<SaleDetailCart> saleDetailCartListObtained = this.getSaleDetailCartList();
            if (saleDetailCartListObtained == null) {
                saleDetailCartListObtained = new ArrayList<>();
                saleDetailCartListObtained.add(saleDetailCart);
                return this;
            }
            saleDetailCartListObtained.add(saleDetailCart);
        }else{
            saleDetailCartObtained.setQuantity(quantity);
            saleDetailCartObtained.calculateSubTotal();
            saleDetailCartObtained.calculateDiscount();
        }
        total();
        totalDiscount();
        totalWithDiscount();
        return this;
    }

    public Cart removeProduct(Product product){
        CartProduct cartProduct = mappToCartProduct(product);
        SaleDetailCart saleDetailCart = searchSailDetailByProduct(cartProduct);
        if(saleDetailCart != null){
            saleDetailCartList.remove(saleDetailCart);
        }
        total();
        totalDiscount();
        totalWithDiscount();
        return this;
    }
    public Cart total(){
        BigDecimal auxTotal = BigDecimal.ZERO;
        for (SaleDetailCart saleDetailCart: saleDetailCartList
             ) {
            auxTotal = auxTotal.add(saleDetailCart.getSubtotal());
        }
        this.total = auxTotal;
        return this;
    }
    public Cart totalDiscount(){
        BigDecimal auxTotalDiscount = BigDecimal.ZERO;
        for (SaleDetailCart saleDetailCart: saleDetailCartList
        ) {
            auxTotalDiscount = auxTotalDiscount.add(saleDetailCart.getDiscount());
        }
        this.discount = auxTotalDiscount;
        return this;
    }
    public Cart totalWithDiscount(){
        BigDecimal auxTotalWithDiscount = BigDecimal.ZERO;
        for (SaleDetailCart saleDetailCart: saleDetailCartList
        ) {
            auxTotalWithDiscount = auxTotalWithDiscount.add(saleDetailCart.getTotalWithDiscount());
        }
        this.totalWithDiscount = auxTotalWithDiscount;
        return this;
    }
    public SaleDetailCart searchSailDetailByProduct(CartProduct product){
        for (SaleDetailCart saleDetailCart: saleDetailCartList
                ) {
            if(saleDetailCart.getProduct().getCode().equals(product.getCode())){
                return saleDetailCart;
            }
        }
        return null;
    }

    public static CartProduct mappToCartProduct(Product product){
        CartProduct cartProduct = new CartProduct();
        cartProduct.setId(product.getId());
        cartProduct.setCode(product.getCode());
        cartProduct.setName(product.getModuleName());
        cartProduct.setPrice(product.getPrice("Local"));
        cartProduct.setDiscount(product.getLocalDiscount());
        return cartProduct;
    }
}
