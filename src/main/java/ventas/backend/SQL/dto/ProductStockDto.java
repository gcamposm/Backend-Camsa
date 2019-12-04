package ventas.backend.SQL.dto;

import lombok.Data;

import java.math.BigDecimal;


public class ProductStockDto {
    private Integer id;
    private String code;
    private Integer stock;
    private Integer warehouseStock;
    private Integer eventStock;
    private BigDecimal cost;
    private String moduleName;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getWarehouseStock() {
        return warehouseStock;
    }

    public void setWarehouseStock(Integer warehouseStock) {
        this.warehouseStock = warehouseStock;
    }

    public Integer getEventStock() {
        return eventStock;
    }

    public void setEventStock(Integer eventStock) {
        if(eventStock == null){
            this.eventStock = 0;
        }else {
            this.eventStock = eventStock;
        }
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}
