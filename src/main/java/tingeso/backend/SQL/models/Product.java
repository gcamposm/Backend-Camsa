package tingeso.backend.SQL.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@SuppressWarnings("Duplicates")
@Entity
@Data
@NoArgsConstructor
@Table(name = "product")
public class Product implements Comparable<Product> {
    // ATTRIBUTES
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    private String code;

    private BigDecimal cost;

    private Integer stock;

    private Integer eventStock;

    private String size;

    private Integer warehouseStock;

    private Boolean published;

    private Boolean deleted;
    //PRICES
    private BigDecimal priceLocal;

    private BigDecimal priceWeb;
    //DISCOUNT
    private Integer localDiscount;

    private Integer webDiscount;

    @Column(columnDefinition="text", length=1000)
    private String description;

    private String moduleName;

    private String webName;

    private Integer minStock;

    //RELATIONS
    //category -> product
    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private SubCategory subCategory;

    //provider -> product
    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Provider provider;

    //type -> product
    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Type type;

    //product -> saleDetail
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SaleDetail> saleDetailList;

    //product -> EntryDetail
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<EntryDetail> entryDetailList;

    //product -> image
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Image> imageList;

    //product -> saleDetail
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Stock> stockList;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ProductAttribute> productAttributeList;

    public BigDecimal getPrice(String type){
        if(type.equals("Local")){
            return priceLocal;
        }
        else if(type.equals("Web")){
            return priceWeb;
        }
        else{
            return null;
        }
    }

    public Integer getDiscount(String type){
        if(type.equals("Local")){
            return localDiscount;
        }
        else if(type.equals("Web")){
            return webDiscount;
        }
        else{
            return null;
        }
    }

    @Override
    public int compareTo(Product o) {
        if (priceWeb.intValue() < o.priceWeb.intValue()) {
            return -1;
        }
        if (priceWeb.intValue() > o.priceWeb.intValue()) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", stock=" + stock +
                ", eventStock=" + eventStock +
                ", warehouseStock=" + warehouseStock +
                '}';
    }

    public Boolean isJokerProduct(){
        return this.code.matches("[0-9]");
    }
}