package tingeso.backend.SQL.models;

import tingeso.backend.SQL.dto.ProductExcel;
import tingeso.backend.SQL.services.ReadService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class Reader implements Runnable {
    @Autowired
    private ReadService readService;

    private List<ProductExcel> productExcelList;

    public Reader(List<ProductExcel> productExcelList){
        this.productExcelList = productExcelList;
    }

    private void saveProductExcelList(){
        System.out.println("tamaño: " + this.productExcelList.size());
        for (ProductExcel productExcel: this.productExcelList
        ) {
            readService.makeProduct(productExcel);
        }
    }
    @Override
    public void run() {
        saveProductExcelList();
    }
}
