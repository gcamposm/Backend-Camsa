package tingeso.backend.SQL.models;

import java.io.*;
import java.util.*;


import tingeso.backend.SQL.dto.ProductExcel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;

@SuppressWarnings("Duplicates")
public class XlsxRead {


    /*public  void readXLSXFile(String filePath) throws IOException
    {
        InputStream ExcelFileToRead = new FileInputStream(filePath);
        XSSFWorkbook  wb = new XSSFWorkbook(ExcelFileToRead);

        XSSFWorkbook test = new XSSFWorkbook();

        XSSFSheet sheet = wb.getSheetAt(0);
        XSSFRow row;
        XSSFCell cell;

        Iterator rows = sheet.rowIterator();
        rows.next();
        while (rows.hasNext())
        {
            ProductExcel productExcel = new ProductExcel();
            row=(XSSFRow) rows.next();
            productExcel.setDescripcion(row.getCell(0).getStringCellValue());
            productExcel.setCosto(Integer.parseInt(row.getCell(1).getStringCellValue()));
            productExcel.setPrecio1(Integer.parseInt(row.getCell(2).getStringCellValue()));
            productExcel.setPrecio2(Integer.parseInt(row.getCell(3).getStringCellValue()));
            productExcel.setPrecio3(Integer.parseInt(row.getCell(4).getStringCellValue()));
            productExcel.setPrecio4(Integer.parseInt(row.getCell(5).getStringCellValue()));
            productExcel.setPrecio5(Integer.parseInt(row.getCell(6).getStringCellValue()));
            productExcel.setCodigo(row.getCell(7).getStringCellValue());
            productExcel.setFamilia(row.getCell(8).getStringCellValue());
            productExcel.setProveedor(row.getCell(9).getStringCellValue());
            productExcel.setMin(Integer.parseInt(row.getCell(10).getStringCellValue()));
            productExcel.setMax(Integer.parseInt(row.getCell(11).getStringCellValue()));
            productExcel.setExs(Integer.parseInt(row.getCell(12).getStringCellValue()));
            productExcel.setCodProveedor(Integer.parseInt(row.getCell(13).getStringCellValue()));
            productExcel.setIva(row.getCell(14).getStringCellValue());
            productExcel.setTipo(row.getCell(15).getStringCellValue());

            //System.out.println(productExcel.toString());
        }

    }*/
    @Autowired
    private ServletContext context;

    public static List<ProductExcel> uploadXLSXFile(InputStream file) throws IOException {
        //InputStream ExcelFileToRead = new FileInputStream("src/main/resources/static/keywords.xlsx");
        XSSFWorkbook wb = new XSSFWorkbook(file);
        XSSFSheet sheet = wb.getSheetAt(0);
        XSSFRow row;

        Iterator rows = sheet.rowIterator();
        rows.next();
        List<ProductExcel> productExcelList = new ArrayList<>();
        rows.hasNext();
        Integer counter = 1;
        while (rows.hasNext())
        {
            ProductExcel productExcel = new ProductExcel();
            row = (XSSFRow) rows.next();
            if(row.getCell(7) != null) {
                productExcel.setModuleName(getStringValue(row.getCell(0)));
                productExcel.setWebName(getStringValue(row.getCell(1)));
                productExcel.setCosto(getDoubleValue(row.getCell(2)));
                productExcel.setPriceLocal(getDoubleValue(row.getCell(3)));
                productExcel.setPriceWeb(getDoubleValue(row.getCell(4)));
                productExcel.setCodigo(getStringValue(row.getCell(7)));
                productExcel.setCategory(getStringValue(row.getCell(8)));
                productExcel.setSubCategory(getStringValue(row.getCell(9)));
                productExcel.setProveedor(getStringValue(row.getCell(10)));
                productExcel.setMin(getIntegerValue(row.getCell(11)));
                productExcel.setMax(getIntegerValue(row.getCell(12)));
                productExcel.setLocalStorage(getIntegerValue(row.getCell(13)));
                productExcel.setWarehouseStorage(getIntegerValue(row.getCell(14)));
                if(row.getCell(15)!=null) {
                    productExcel.setDescripcion(getStringValue(row.getCell(15)));
                }
                if(row.getCell(16) != null) {
                    productExcel.setSize(getStringValue(row.getCell(16)));
                }
                productExcelList.add(productExcel);
                System.out.println("Number: " + counter + " " + productExcel.toString());
            }
            counter++;
        }

        return productExcelList;
    }
    public  void writeXlsx(List<Product> productList) throws IOException{
        //File myFile = new File("src/main/resources/static/output4.xlsx");
        //FileOutputStream generated = new FileOutputStream(myFile);

        XSSFWorkbook myWorkBook = new XSSFWorkbook();
        //FileOutputStream fileOut1 = new FileOutputStream("src/main/resources/static/output4.xlsx");
        //myWorkBook.write(fileOut1);
        myWorkBook.createSheet("Sheet1");
        //fileOut1.close();
        //File file = new File("src/main/resources/static/CatalogoDeProductosfINAL3.xlsx");
        //FileInputStream fis = new FileInputStream(file);
        // Finds the workbook instance for XLSX file
        //XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);

        // Return first sheet from the XLSX workbook
        XSSFSheet mySheet = myWorkBook.getSheetAt(0);

        // Get iterator to all the rows in current sheet
        Iterator<Row> rowIterator = mySheet.iterator();
        Map<String, Object[]> data = new HashMap<String, Object[]>();
        data.put("7", new Object[] {7d, "Sonya", "75K", "SALES", "Rupert"});
        data.put("8", new Object[] {8d, "Kris", "85K", "SALES", "Rupert"});
        data.put("9", new Object[] {9d, "Dave", "90K", "SALES", "Rupert"});


        // Set to Iterate and add rows into XLS file
        Set<String> newRows = data.keySet();
        System.out.println(newRows);

        // get the last row number to append new data
        int rownum = mySheet.getLastRowNum();

        /*for (String key : newRows) {

            // Creating a new Row in existing XLSX sheet
            Row row = mySheet.createRow(rownum++);
            Object [] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell = row.createCell(cellnum++);
                if (obj instanceof String) {
                    cell.setCellValue((String) obj);
                } else if (obj instanceof Boolean) {
                    cell.setCellValue((Boolean) obj);
                } else if (obj instanceof Date) {
                    cell.setCellValue((Date) obj);
                } else if (obj instanceof Double) {
                    cell.setCellValue((Double) obj);
                }
            }
        }*/

        Row headerRow = mySheet.createRow(rownum++);
        String[] columns = {"Nombre Modulo Venta", "Nombre Web", "Costo", "Precio1 (Normal)", "Precio2 (Evento)", "Precio3 (5% dscto)", "Precio4 (10% dscto.)", "Código", "CATEGORIA MADRE", "CATEGORIA 1", "Proveedor", "Min.", "Max.", "Inv. Tienda", "Inv. Bodega", "Descripcion", "Talla"};
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        for(Product product: productList) {
            Row row = mySheet.createRow(rownum++);

            row.createCell(0)
                    .setCellValue(product.getModuleName());
            row.createCell(1)
                    .setCellValue(product.getWebName());
            row.createCell(2)
                    .setCellValue(product.getCost().toString());
            row.createCell(3)
                    .setCellValue(product.getPriceWeb().toString());
            row.createCell(4)
                    .setCellValue(product.getPriceWeb().toString());
            row.createCell(5)
                    .setCellValue(product.getPriceLocal().toString());
            row.createCell(6)
                    .setCellValue(product.getPriceLocal().toString());
            row.createCell(7)
                    .setCellValue(product.getCode());
            row.createCell(8)
                    .setCellValue(product.getSubCategory().getCategory().getName());
            row.createCell(9)
                    .setCellValue(product.getSubCategory().getName());
            row.createCell(10)
                    .setCellValue(product.getProvider().getName());
            row.createCell(11)
                    .setCellValue(0);
            row.createCell(12)
                    .setCellValue(10);
            row.createCell(13)
                    .setCellValue(product.getStock());
            row.createCell(14)
                    .setCellValue(product.getWarehouseStock());
            row.createCell(15)
                    .setCellValue(product.getDescription());
            row.createCell(16)
                    .setCellValue(product.getSize());
        }
        for(int i = 0; i < columns.length; i++) {
            mySheet.autoSizeColumn(i);
        }

        // open an OutputStream to save written data into XLSX file
        String relativeWebPath = "WEB-INF/classes/static";
        String absoluteFilePath = context.getRealPath(relativeWebPath);
        //String absoluteFilePath = "src/main/resources/static/output2.xlsx";
        FileOutputStream os = new FileOutputStream(absoluteFilePath);
        myWorkBook.write(os);
        os.close();
        myWorkBook.close();
        System.out.println("Writing on XLSX file Finished ...");
    }
    public static double getDoubleValue(Cell cell){
        switch (cell.getCellType()) {
            case STRING:
                return Integer.parseInt(cell.getRichStringCellValue().getString());
            case NUMERIC:
                return (cell.getNumericCellValue());
            case BLANK:
                return 0;
            default:
                return -1;
        }
    }
    public static int getIntegerValue(Cell cell){
        switch (cell.getCellType()) {
            case STRING:
                return Integer.parseInt(cell.getRichStringCellValue().getString());
            case NUMERIC:
                return (int)(cell.getNumericCellValue());
            case BLANK:
                return 0;
            default:
                return -1;
        }
    }
    public static String getStringValue(Cell cell){
        switch (cell.getCellType()) {
            case STRING:
                String content;
                content = cell.getRichStringCellValue().getString();
                if(content.length() > 1000){
                    return content.substring(0,999);
                }
                return cell.getRichStringCellValue().getString();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BLANK:
                return "";
            default:
                return "Error de formato";
        }
    }
}
