package ventas.backend.SQL.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "sale_detail")
@JsonIgnoreProperties({"sale"})
public class SaleDetail {
    // ATTRIBUTES
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    private Integer quantity;

    @NonNull
    private BigDecimal discount;

    @NonNull
    private BigDecimal subtotal;

    @NonNull
    private BigDecimal unitaryPrice;

    @NonNull
    private BigDecimal totalWithDiscount;
    // RELATIONS
    private String productName;

    // product -> saleDetail

    @NonNull
    @ManyToOne
    @JoinColumn
    private Product product;

    // sale -> saleDetail

    @ManyToOne
    @JoinColumn
    @NonNull
    private Sale sale;

    public SaleDetail() {
        this.quantity = 0;
        this.discount = BigDecimal.ZERO;
        this.subtotal = BigDecimal.ZERO;
        this.unitaryPrice = BigDecimal.ZERO;
        this.totalWithDiscount = BigDecimal.ZERO;
    }

    public SaleDetail(@NonNull Integer quantity) {
        this.quantity = 0;
    }

    public void sumItems(Product product, String type){
        BigDecimal subtotalBigDecimal = BigDecimal.valueOf(quantity);
        System.out.println("sumItems: quantity: " + subtotalBigDecimal + " price: " + product.getPrice(type));
        this.subtotal = product.getPrice(type).multiply(subtotalBigDecimal);
    }
    public void calculateSubTotal(){
        this.subtotal = unitaryPrice.multiply(BigDecimal.valueOf(quantity));
    }
    public void restItems(Integer quantity, Product product, String type){
        BigDecimal subtotalBigDecimal = BigDecimal.valueOf(quantity);
        this.subtotal = product.getPrice(type).multiply(subtotalBigDecimal);
    }
    public void calculateUnitaryPrice(String type){
        int productDiscount = product.getDiscount(type);
        double discountFactor = productDiscount/100.0;
        BigDecimal bigDiscountFactor = BigDecimal.valueOf(1 - discountFactor);
        this.unitaryPrice = product.getPrice(type).multiply(bigDiscountFactor);
    }
    //Arreglar este método para evitar inconsistencias de precio
    public void calculateDiscount(String type){
        int productDiscount = product.getDiscount(type);
        double discountFactor = productDiscount/100.0;
        BigDecimal bigDiscountFactor = BigDecimal.valueOf(discountFactor);
        this.discount = subtotal.multiply(bigDiscountFactor);
        this.totalWithDiscount = this.subtotal.subtract(this.discount);
    }
    public void actualizeProductName(String type){
        if(type.equals("Local")) {
            this.productName = this.product.getModuleName();
        }
        if(type.equals("web")){
            this.productName = this.product.getWebName();
        }
    }
    public void actualizeDiscountMount(){
        this.totalWithDiscount = subtotal.subtract(discount);
    }
    //methods for new fast sale create
    public BigDecimal calculateProductDiscount(String type){
        int productDiscount = product.getDiscount(type);
        double discountFactor = productDiscount/100.0;
        BigDecimal bigDiscountFactor = BigDecimal.valueOf(discountFactor);
        return subtotal.multiply(bigDiscountFactor);
    }
}
