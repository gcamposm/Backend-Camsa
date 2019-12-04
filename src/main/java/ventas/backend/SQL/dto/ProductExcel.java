package ventas.backend.SQL.dto;

import lombok.Data;

@Data
public class ProductExcel {
    private String moduleName;
    private String descripcion;
    private Double costo;
    private Double priceLocal;
    private Double priceWeb;
    private String codigo;
    private String category;
    private String subCategory;
    private String proveedor;
    private Integer min;
    private Integer max;
    private Integer localStorage;
    private Integer warehouseStorage;
    private String webName;
    private String size;


    public ProductExcel(String moduleName, String descripcion, Double costo, Double priceLocal, Double priceWeb, String codigo, String category, String subCategory, String proveedor, Integer min, Integer max, Integer localStorage, Integer warehouseStorage, String webName, String size) {
        this.moduleName = moduleName;
        this.descripcion = descripcion;
        this.costo = costo;
        this.priceLocal = priceLocal;
        this.priceWeb = priceWeb;
        this.codigo = codigo;
        this.category = category;
        this.subCategory = subCategory;
        this.proveedor = proveedor;
        this.min = min;
        this.max = max;
        this.localStorage = localStorage;
        this.warehouseStorage = warehouseStorage;
        this.webName = webName;
        this.size = size;
    }

    public ProductExcel() {
    }

    @Override
    public String toString() {
        return "ProductExcel{" +
                "moduleName='" + moduleName + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", costo=" + costo +
                ", priceLocal=" + priceLocal +
                ", priceWeb=" + priceWeb +
                ", codigo='" + codigo + '\'' +
                ", category='" + category + '\'' +
                ", subCategory='" + subCategory + '\'' +
                ", proveedor='" + proveedor + '\'' +
                ", min=" + min +
                ", max=" + max +
                ", localStorage=" + localStorage +
                ", webName=" + webName +
                ", warehouseStorage=" + warehouseStorage +
                ", size=" + size +
                '}';
    }
}
