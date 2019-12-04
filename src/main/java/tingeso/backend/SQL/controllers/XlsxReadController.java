package tingeso.backend.SQL.controllers;

import tingeso.backend.FileManagement.service.FileStorageService;
import tingeso.backend.SQL.dto.ProductExcel;
import tingeso.backend.SQL.models.Product;
import tingeso.backend.SQL.models.XlsxRead;
import tingeso.backend.SQL.services.ProductService;
import tingeso.backend.SQL.services.ReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/upload")
public class XlsxReadController {
    @Autowired
    private ReadService readService;

    @Autowired
    private ProductService productService;

    @Autowired
    ServletContext context;

    @Autowired
    private FileStorageService fileStorageService;

    private Integer totalProducts = 0;
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(value = "/read", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file) throws IOException
    {
        ReadService.currentProducts = 0;
        //String relativeWebPath = "WEB-INF/classes/static";
        //String absoluteFilePath = context.getRealPath(relativeWebPath);
        //String absoluteFilePath = "/Users/eduardopailemilla/MacbookAir/files/";
        //Path absoluteFilePath = fileStorageService.getFileStorageLocation();
        String absoluteFilePath = fileStorageService.storeFileAndReturnPath(file);
        File convertFile = new File(absoluteFilePath);
        try(FileInputStream fileInputStream = new FileInputStream(convertFile)) {
            List<ProductExcel> productExcelList = XlsxRead.uploadXLSXFile(fileInputStream);
            totalProducts = productExcelList.size();
            this.saveProductExcelList(productExcelList);
            //CODIGO A BORRAR SI ES NECESARIO
            /*System.out.println("Largo: " + productExcelList.size()/2);
            List<ProductExcel> productExcelList1 = productExcelList.subList(0, productExcelList.size()/2);
            List<ProductExcel> productExcelList2 = productExcelList.subList(productExcelList.size()/2 + 1, productExcelList.size() - 1);
            Thread t1 = new Thread(() -> {
                // code goes here.
                saveProductExcelList(productExcelList1);
            });
            Thread t2 = new Thread(() -> {
                // code goes here.
                saveProductExcelList(productExcelList2);
            });
            t1.start();
            t2.start();
            t1.join();
            t2.join();*/
            //-----------------------------------------------
            fileInputStream.close();
        }
        catch(IOException ex) {
            return new ResponseEntity<>("Exception occurred while zipping file", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>("file is uploaded successfully", HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(value = "/readWithoutStock", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> readWithoutStock(@RequestParam("file") MultipartFile file) throws IOException
    {
        ReadService.currentProducts = 0;
        //String relativeWebPath = "WEB-INF/classes/static";
        //String absoluteFilePath = context.getRealPath(relativeWebPath);
        //String absoluteFilePath = "/Users/eduardopailemilla/MacbookAir/files/";
        //Path absoluteFilePath = fileStorageService.getFileStorageLocation();
        String absoluteFilePath = fileStorageService.storeFileAndReturnPath(file);
        File convertFile = new File(absoluteFilePath);
        try(FileInputStream fileInputStream = new FileInputStream(convertFile)) {
            List<ProductExcel> productExcelList = XlsxRead.uploadXLSXFile(fileInputStream);
            totalProducts = productExcelList.size();
            this.saveProductExcelListWithoutStock(productExcelList);
            //CODIGO A BORRAR SI ES NECESARIO
            /*System.out.println("Largo: " + productExcelList.size()/2);
            List<ProductExcel> productExcelList1 = productExcelList.subList(0, productExcelList.size()/2);
            List<ProductExcel> productExcelList2 = productExcelList.subList(productExcelList.size()/2 + 1, productExcelList.size() - 1);
            Thread t1 = new Thread(() -> {
                // code goes here.
                saveProductExcelList(productExcelList1);
            });
            Thread t2 = new Thread(() -> {
                // code goes here.
                saveProductExcelList(productExcelList2);
            });
            t1.start();
            t2.start();
            t1.join();
            t2.join();*/
            //-----------------------------------------------
            fileInputStream.close();
        }
        catch(IOException ex) {
            return new ResponseEntity<>("Exception occurred while zipping file", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>("file is uploaded successfully", HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(value = "/write", method = RequestMethod.GET)
    public ResponseEntity<Object> writeFile() throws IOException
    {
        List<Product> productList = productService.getProductsForExcel();
        if(productList == null)
        {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body("No existen productos");
        }
        XlsxRead xlsxRead = new XlsxRead();
        xlsxRead.writeXlsx(productList);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Se creó el archivo de salida.");
    }
    
    @GetMapping("/percentage")
    public double getPercentage(){
        double currentProductNumber = ReadService.currentProducts;
        if(totalProducts == 0){
            return 0;
        }
        return (currentProductNumber*100/totalProducts);
    }
    private void saveProductExcelList(List<ProductExcel> productExcelList){
        for (ProductExcel productExcel: productExcelList
             ) {
            readService.makeProduct(productExcel);
        }
    }

    private void saveProductExcelListWithoutStock(List<ProductExcel> productExcelList){
        for (ProductExcel productExcel: productExcelList
        ) {
            readService.makeProductWithoutStock(productExcel);
        }
    }
}
