package tingeso.backend.SQL.dto;

import tingeso.backend.SQL.models.Image;
import tingeso.backend.SQL.models.Provider;
import tingeso.backend.SQL.models.SubCategory;
import java.math.BigDecimal;
import java.util.List;

public interface ProductInterfaceDto {
    void setSize(String size);

    String getSize();
    Integer getId();

    void setId(Integer id);
    String getCode();

    void setCode(String code);

    Integer getQuantity();

    void setQuantity(Integer quantity);

    BigDecimal getCost();

    void setCost(BigDecimal cost);

    Integer getStock();

    void setStock(Integer stock);

    Integer getEventStock();

    void setEventStock(Integer eventStock);

    Integer getWarehouseStock();

    void setWarehouseStock(Integer warehouseStock);

    BigDecimal getPriceLocal();

    void setPriceLocal(BigDecimal priceLocal);

    BigDecimal getPriceWeb();
    void setPriceWeb(BigDecimal priceWeb);

    Integer getLocalDiscount();

    void setLocalDiscount(Integer localDiscount);
    Integer getWebDiscount();

    void setWebDiscount(Integer webDiscount);

    String getDescription();

    void setDescription(String description);

    String getModuleName();

    void setModuleName(String moduleName);

    String getWebName();

    void setWebName(String webName);

    SubCategory getSubCategory();
    void setSubCategory(SubCategory subCategory);

    Provider getProvider();

    void setProvider(Provider provider);

    List<Image> getImageList();

    void setImageList(List<Image> imageList);

    Boolean getPublished();

    void setPublished(Boolean published);

    Boolean getDeleted();
    void setDeleted(Boolean deleted);
}
