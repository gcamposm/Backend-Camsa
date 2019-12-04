package ventas.backend.SQL.services;

import tingeso.backend.SQL.dao.*;
import ventas.backend.SQL.dao.CategoryDao;
import ventas.backend.SQL.dao.ProductDao;
import ventas.backend.SQL.dao.ProviderDao;
import ventas.backend.SQL.dao.SubCategoryDao;
import ventas.backend.SQL.dto.ProductExcel;
import tingeso.backend.SQL.models.*;
import ventas.backend.SQL.models.Category;
import ventas.backend.SQL.models.Product;
import ventas.backend.SQL.models.Provider;
import ventas.backend.SQL.models.SubCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ReadService {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private SubCategoryDao subCategoryDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProviderDao providerDao;

    public  static Integer currentProducts = 0;

    public void makeProduct(ProductExcel productExcel){
        String code = productExcel.getCodigo();
        Product product = productDao.findProductByCode(code);
        currentProducts++;
        if(product == null){
            saveProduct(productExcel);
        } else{
            updateProduct(productExcel, product);
        }
    }

    public void makeProductWithoutStock(ProductExcel productExcel){
        String code = productExcel.getCodigo();
        Product product = productDao.findProductByCode(code);
        currentProducts++;
        if(product == null){
            saveProduct(productExcel);
        } else{
            updateProductWithoutStock(productExcel, product);
        }
    }

    private void saveProduct(ProductExcel productExcel){
        Category category = categoryDao.findByName(productExcel.getCategory());
        SubCategory subCategory = subCategoryDao.findSubCategoriesByName(productExcel.getSubCategory());
        Provider provider = providerDao.findProviderByName(productExcel.getProveedor());
        if(provider == null){
            provider = new Provider();
            provider.setName(productExcel.getProveedor());
            provider.setDeleted(false);
            providerDao.save(provider);
        }
        if(category == null){
            category = new Category();
            category.setName(productExcel.getCategory());
            category.setDeleted(false);
            categoryDao.save(category);
        }
        if(subCategory == null){
            subCategory = new SubCategory();
            subCategory.setName(productExcel.getSubCategory());
            subCategory.setCategory(category);
            subCategory.setDeleted(false);
            subCategoryDao.save(subCategory);
        }
        Product product = this.makeProductHelper(productExcel);
        productDao.save(product);
    }

    private void updateProduct(ProductExcel productExcel, Product product){
        Category category = categoryDao.findByName(productExcel.getCategory());
        SubCategory subCategory = subCategoryDao.findSubCategoriesByName(productExcel.getSubCategory());
        Provider provider = providerDao.findProviderByName(productExcel.getProveedor());
        if(provider == null){
            provider = new Provider();
            provider.setName(productExcel.getProveedor());
            providerDao.save(provider);
        }
        if(category == null){
            category = new Category();
            category.setName(productExcel.getCategory());
            categoryDao.save(category);
        }
        if(subCategory == null){
            subCategory = new SubCategory();
            subCategory.setName(productExcel.getSubCategory());
            subCategory.setCategory(category);
            subCategoryDao.save(subCategory);
        }
        product = updateProductHelper(productExcel, product);
        productDao.save(product);
    }

    private void updateProductWithoutStock(ProductExcel productExcel, Product product){
        Category category = categoryDao.findByName(productExcel.getCategory());
        SubCategory subCategory = subCategoryDao.findSubCategoriesByName(productExcel.getSubCategory());
        Provider provider = providerDao.findProviderByName(productExcel.getProveedor());
        if(provider == null){
            provider = new Provider();
            provider.setName(productExcel.getProveedor());
            providerDao.save(provider);
        }
        if(category == null){
            category = new Category();
            category.setName(productExcel.getCategory());
            categoryDao.save(category);
        }
        if(subCategory == null){
            subCategory = new SubCategory();
            subCategory.setName(productExcel.getSubCategory());
            subCategory.setCategory(category);
            subCategoryDao.save(subCategory);
        }
        product = updateProductHelperWithoutStock(productExcel, product);
        productDao.save(product);
    }

    private Product makeProductHelper(ProductExcel productExcel){
        Product product = new Product();
        product.setStock(productExcel.getLocalStorage());
        product.setWarehouseStock(productExcel.getWarehouseStorage());
        product.setModuleName(productExcel.getModuleName());
        product.setCode(productExcel.getCodigo());
        product.setDescription(productExcel.getDescripcion());
        product.setWebName(productExcel.getWebName());
        product.setCode(productExcel.getCodigo());
        product.setDeleted(false);
        product.setPublished(true);
        product.setSize(productExcel.getSize());
        //PRICE AND COST
        Double costI = productExcel.getCosto();
        Double priceLocalInteger = productExcel.getPriceLocal();
        Double priceWebInteger = productExcel.getPriceWeb();
        java.math.BigDecimal cost = new java.math.BigDecimal(costI.toString());
        java.math.BigDecimal priceLocal = new java.math.BigDecimal(priceLocalInteger.toString());
        java.math.BigDecimal priceWeb = new java.math.BigDecimal(priceWebInteger.toString());
        product.setCost(cost);
        //PRICES
        product.setPriceLocal(priceLocal);
        product.setPriceWeb(priceWeb);
        //DISCOUNTS
        Integer initialDiscount = 0;
        product.setLocalDiscount(initialDiscount);
        product.setWebDiscount(initialDiscount);
        //TYPE AND CATEGORY
        SubCategory subCategory = subCategoryDao.findSubCategoriesByName(productExcel.getSubCategory());
        Provider provider = providerDao.findProviderByName(productExcel.getProveedor());
        product.setProvider(provider);
        product.setSubCategory(subCategory);
        return product;
    }

    private Product updateProductHelper(ProductExcel productExcel, Product product){
        product.setStock(productExcel.getLocalStorage());
        product.setWarehouseStock(productExcel.getWarehouseStorage());
        product.setModuleName(productExcel.getModuleName());
        //product.setIva(productExcel.getIva());
        product.setCode(productExcel.getCodigo());
        product.setDescription(productExcel.getDescripcion());
        product.setCode(productExcel.getCodigo());
        product.setSize(productExcel.getSize());
        //PRICE AND COST
        Double costI = productExcel.getCosto();
        Double priceLocalInteger = productExcel.getPriceLocal();
        Double priceWebInteger = productExcel.getPriceWeb();
        java.math.BigDecimal cost = new java.math.BigDecimal(costI.toString());
        java.math.BigDecimal priceLocal = new java.math.BigDecimal(priceLocalInteger.toString());
        java.math.BigDecimal priceWeb = new java.math.BigDecimal(priceWebInteger.toString());
        product.setCost(cost);
        product.setPriceLocal(priceLocal);
        product.setPriceWeb(priceWeb);
        //TYPE AND CATEGORY
        SubCategory subCategory = subCategoryDao.findSubCategoriesByName(productExcel.getSubCategory());
        Provider provider = providerDao.findProviderByName(productExcel.getProveedor());
        product.setProvider(provider);
        product.setSubCategory(subCategory);
        return product;
    }

    private Product updateProductHelperWithoutStock(ProductExcel productExcel, Product product){
        product.setModuleName(productExcel.getModuleName());
        //product.setIva(productExcel.getIva());
        product.setCode(productExcel.getCodigo());
        product.setDescription(productExcel.getDescripcion());
        product.setCode(productExcel.getCodigo());
        product.setSize(productExcel.getSize());
        //PRICE AND COST
        Double costI = productExcel.getCosto();
        Double priceLocalInteger = productExcel.getPriceLocal();
        Double priceWebInteger = productExcel.getPriceWeb();
        java.math.BigDecimal cost = new java.math.BigDecimal(costI.toString());
        java.math.BigDecimal priceLocal = new java.math.BigDecimal(priceLocalInteger.toString());
        java.math.BigDecimal priceWeb = new java.math.BigDecimal(priceWebInteger.toString());
        product.setCost(cost);
        product.setPriceLocal(priceLocal);
        product.setPriceWeb(priceWeb);
        //TYPE AND CATEGORY
        SubCategory subCategory = subCategoryDao.findSubCategoriesByName(productExcel.getSubCategory());
        Provider provider = providerDao.findProviderByName(productExcel.getProveedor());
        product.setProvider(provider);
        product.setSubCategory(subCategory);
        return product;
    }
}