package tingeso.backend.SQL.dto;

import tingeso.backend.SQL.models.Image;
import tingeso.backend.SQL.models.Provider;
import tingeso.backend.SQL.models.SubCategory;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OneProductDto implements ProductInterfaceDto{
    private Integer id;

    private String code;

    private Integer quantity = 1;

    private BigDecimal cost;

    private Integer stock;

    private Integer eventStock;

    private Integer warehouseStock;

    private Boolean published;

    private Boolean deleted;

    private String size;
    //-----------------------------
    //PRICES
    private BigDecimal priceLocal;

    private BigDecimal priceWeb;


    //DISCOUNT
    private Integer localDiscount;

    private Integer webDiscount;

    //-----------------------------

    private String description;

    private String moduleName;

    private String webName;
    // RELATIONS

    // category -> product
    private SubCategory subCategory;

    // provider -> product
    private Provider provider;


    private List<Image> imageList;


}
