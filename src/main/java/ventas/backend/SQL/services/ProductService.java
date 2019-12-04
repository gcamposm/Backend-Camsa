package ventas.backend.SQL.services;

import ventas.backend.Exceptions.CodeNotFoundException;
import ventas.backend.Exceptions.StatisticException;
import ventas.backend.FileManagement.service.FileStorageService;
import ventas.backend.SQL.dao.CategoryDao;
import ventas.backend.SQL.dao.ImageDao;
import ventas.backend.SQL.dao.ProductDao;
import ventas.backend.SQL.dao.SubCategoryDao;
import ventas.backend.SQL.daoPagination.ProductPaginationDao;
import ventas.backend.SQL.dto.ProductInterfaceDto;
import ventas.backend.SQL.dto.ProductStockDto;
import ventas.backend.SQL.mappers.ProductInterfaceMapper;
import ventas.backend.SQL.mappers.ProductStockMapper;
import ventas.backend.SQL.models.Category;
import ventas.backend.SQL.models.Image;
import ventas.backend.SQL.models.Product;
import ventas.backend.SQL.models.SubCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.*;

@SuppressWarnings("Duplicates")
@Service
public class ProductService {

    @Autowired
    ServletContext context;

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private SubCategoryDao subCategoryDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private ProductPaginationDao productPaginationDao;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ProductStockMapper productStockMapper;

    @Autowired
    private ProductInterfaceMapper productInterfaceMapper;

    public List<Product> getProductsForExcel(){
        return productDao.findAll();
    }

    public List<ProductInterfaceDto> getAllProduct(){
        List<Product> productList = productDao.findProductsByDeletedFalse();
        List<Product> productListAux = new ArrayList<>();
        if(productList.size() > 0){
            productList.forEach(product -> {
                if(!product.isJokerProduct()){
                    productListAux.add(product);
                }
            });
        }
        return productInterfaceMapper.mapToDtoArrayList(productListAux, "productDto");
    }

    public ProductInterfaceDto getProductByID(Integer id){
        if(productDao.findById(id).isPresent()){
            return productInterfaceMapper.mapToDto(productDao.findProductById(id), "oneProductDto");
        }
        else{
            return  null;
        }
    }
    public ProductInterfaceDto updateProduct(ProductInterfaceDto productDto, Integer id){
        if(productDao.findById(id).isPresent()){
            Product product = productDao.findProductById(id);
            product.setId(productDto.getId());
            product.setSubCategory(productDto.getSubCategory());
            product.setCode(productDto.getCode());
            product.setCost(productDto.getCost());
            product.setPriceLocal(productDto.getPriceLocal());
            product.setPriceWeb(productDto.getPriceWeb());
            product.setProvider(productDto.getProvider());
            product.setDescription(productDto.getDescription());
            product.setStock(productDto.getStock());
            product.setModuleName(productDto.getModuleName());
            product.setWarehouseStock(productDto.getWarehouseStock());
            product.setLocalDiscount(productDto.getLocalDiscount());
            product.setWebDiscount(productDto.getWebDiscount());
            product.setWebName(productDto.getWebName());
            product.setPublished(productDto.getPublished());
            product.setDeleted(productDto.getDeleted());
            product.setSize(productDto.getSize());
            product.setEventStock(productDto.getEventStock());
            return productInterfaceMapper.mapToDto(productDao.save(product),"oneProductDto");
        }
        return null;
    }
    public ProductInterfaceDto deleteProduct(Integer id){
        Product product = productDao.findProductById(id);
        product.setDeleted(true);
        return productInterfaceMapper.mapToDto(productDao.save(product), "oneProductDto");
    }

    public int getTotalProduct(){
        return productDao.getProductQuantity();
    }

    public ProductInterfaceDto createProduct(ProductInterfaceDto oneProductDto){
        oneProductDto.setDeleted(false);
        return productInterfaceMapper.mapToDto(productDao.save(productInterfaceMapper.mapToModel(oneProductDto, "oneProductDto")), "oneProductDto");
    }

    public List<ProductInterfaceDto> getAllProductsForWeb(){
            List<Product> productList = getProductListForWeb();
            return productInterfaceMapper.mapToDtoArrayList(productList, "productDto");
    }

    public List<Product> getProductListForWeb(){
        List<Product> productList = productDao.findWebProducts();
        List<Product> productListAux = new ArrayList<>();
        for (Product product:productList
        ) {
            if(product.getImageList().size() != 0)
            {
                productListAux.add(product);
            }
        }
        return productListAux;
    }

    public Product uploadImage(Product productToUpdate, String name, byte[] fileBytes) throws IOException {
        String ext = name.substring(name.lastIndexOf("."));
        //String relativeWebPath = "WEB-INF/classes/static/imagenes";
        //String absoluteFilePath = context.getRealPath(relativeWebPath);
        //String absoluteFilePath = "src/main/resources/static/imagenes/";
        Path absoluteFilePath = fileStorageService.getFileStorageLocation();
        Integer index = 0;
        if (imageDao.findTopByOrderByIdDesc() != null)
        {
            index = imageDao.findTopByOrderByIdDesc().getId()+1;
        }
        String fileName = productToUpdate.getCode() + "_" + index.toString();
        File convertFile = new File(absoluteFilePath + "/" + fileName + ext);
        try(FileOutputStream fos = new FileOutputStream(convertFile)) {
            byte[] bytes = fileBytes;
            fos.write(bytes);
            Image image = new Image();
            image.setProduct(productToUpdate);
            image.setName(fileName);
            image.setExtension(ext);
            image.setPrincipal(false);
            List<Image> imageList = productToUpdate.getImageList();
            if (imageList.size() == 0) {
                image.setPrincipal(true);
            }
            imageList.add(image);
            productToUpdate.setImageList(imageList);
            for (Image image1 : productToUpdate.getImageList()
            ) {
                System.out.println("imagen: " + image1.getName() + "" + image1.getExtension());
            }
            return productToUpdate;
        }catch(IOException IEX){
            return null;
        }
    }

    public ProductInterfaceDto getJokerProduct(BigDecimal price) {
        BigDecimal zero = new BigDecimal(0);
        Product product = new Product();
        product.setCode("00000");
        product.setPriceLocal(price);
        product.setPriceWeb(zero);
        product.setSize("S");
        product.setStock(1000);
        product.setDeleted(false);
        product.setPublished(false);
        product.setModuleName("");
        product.setWebName("");
        return productInterfaceMapper.mapToDto(productDao.save(product), "oneProductDto");
    }

    public List<ProductInterfaceDto> getProductsBySubCategoryId(Integer id) {
        SubCategory subCategory = subCategoryDao.findSubCategoriesById(id);
        List<Product> productList = productDao.findProductsBySubCategoryAndDeleted(subCategory, false);
        List<Product> productListAux = new ArrayList<>();
        for (Product product:productList
        ) {
            if(product.getImageList().size() != 0)
            {
                productListAux.add(product);
            }
        }
        return productInterfaceMapper.mapToDtoArrayList(productListAux, "productDto");
    }

    public List<Product> getProductList(Product productToRandom){
        List<Product> productList = productDao.findWebProducts();
        List<Product> finalProductList = productDao.findWebProducts();
        for (Product product:productList
             ) {
            //if(product.getImageList().equals(new ArrayList<>()) || productToRandom.equals(product))
            if(productToRandom.getId().equals(product.getId()) || product.getImageList().size() == 0)
            {
                finalProductList.remove(product);
            }
        }
        return finalProductList;
    }

    public List<Product> getProductListToRandom(Product productToRandom){
        List<Product> productList = productDao.findWebProducts();
        List<Product> finalProductList = productDao.findWebProducts();
        for (Product product:productList
        ) {
            //if(product.getImageList().equals(new ArrayList<>()) || productToRandom.equals(product))
            if(productToRandom.getId().equals(product.getId()) || product.getImageList().size() == 0 || product.getStock() == 0)
            {
                finalProductList.remove(product);
            }
        }
        return finalProductList;
    }

    public Object getProductsRandom(Integer id) {
        Random random = new Random();
        Product product = productDao.findProductById(id);
        List<Product> randomProductList = new ArrayList<>();
        List<Product> productList = getProductListToRandom(product);
        if (productList.size() >= 4) {
            Integer random1 = random.nextInt(productList.size() - 1);
            Integer random2 = random.nextInt(productList.size() - 1);
            while (random2.equals(random1)) {
                random2 = random.nextInt(productList.size() - 1);
            }
            Integer random3 = random.nextInt(productList.size() - 1);
            while (random3.equals(random2) || random3.equals(random1)) {
                random3 = random.nextInt(productList.size() - 1);
            }
            Integer random4 = random.nextInt(productList.size() - 1);
            while (random4.equals(random3) || random4.equals(random2) || random4.equals(random1)) {
                random4 = random.nextInt(productList.size() - 1);
            }
            Product product1 = productList.get(random1);
            Product product2 = productList.get(random2);
            Product product3 = productList.get(random3);
            Product product4 = productList.get(random4);
            randomProductList.add(product1);
            randomProductList.add(product2);
            randomProductList.add(product3);
            randomProductList.add(product4);
            return randomProductList;
        }
        return null;
    }

    public List<Product> filterName(List<Product> productList, String name)
    {
        for (Product product: productList
             ) {
            if(!product.getWebName().equals(name))
            {
                productList.remove(product);
            }
        }
        return productList;
    }

    public List<Product> filterMinPrice(List<Product> productList, Integer minPrice)
    {
        for (Product product: productList
        ) {
            if(product.getPriceWeb().intValue() < minPrice)
            {
                productList.remove(product);
            }
        }
        return productList;
    }

    public List<Product> filterMaxPrice(List<Product> productList, Integer maxPrice)
    {
        for (Product product: productList
        ) {
            if(product.getPriceWeb().intValue() > maxPrice)
            {
                productList.remove(product);
            }
        }
        return productList;
    }

    public List<Product> filterCategory(List<Product> productList, Category category)
    {
        for (Product product: productList
        ) {
            if(!product.getSubCategory().getCategory().equals(category))
            {
                productList.remove(product);
            }
        }
        return productList;
    }

    public List<Product> filterSubCategory(List<Product> productList, SubCategory subCategory)
    {
        for (Product product: productList
        ) {
            if(!product.getSubCategory().equals(subCategory))
            {
                productList.remove(product);
            }
        }
        return productList;
    }

    public List<Product> filterForWeb(String name, Integer minPrice, Integer maxPrice, Category category, SubCategory subCategory, List<Product> productList) {
        if(!name.equals(""))
        {
            productList = filterName(productList, name);
        }
        if(minPrice != 0)
        {
            productList = filterMinPrice(productList, minPrice);
        }
        if(maxPrice != 0 && maxPrice>minPrice)
        {
            productList = filterMaxPrice(productList, maxPrice);
        }
        if(category != null)
        {
            productList = filterCategory(productList, category);
        }
        if(subCategory != null)
        {
            productList = filterSubCategory(productList, subCategory);
        }
        return productList;
    }

    public Object orderForWeb(String order, List<Product> productList) {
        Collections.sort(productList);
        if(order.equals("down")){
            Collections.reverse(productList);
        }
        return productList;
    }

    public List<Product> getProductsByCategoryId(Integer id) {
        Category category = categoryDao.findCategoryById(id);
        List<Product> products = new ArrayList<>();
        if(category != null){
            List<SubCategory> subCategoryList = category.getSubCategoryList();
            subCategoryList.forEach(subCategory -> {
                products.addAll(subCategory.getProductList());
            });
            List<Product> productListAux = new ArrayList<>();
            for (Product product:products
            ) {
                if(product.getImageList().size() != 0)
                {
                    productListAux.add(product);
                }
            }
            return productListAux;
        }
        return new ArrayList<>();
    }

    public List<Product> getProductByPagination(Integer from, Integer to){
        Pageable firstPageWithTwoElements = PageRequest.of(from, to);
        List<Product> productList = productPaginationDao.findByOrderByModuleNameAsc(firstPageWithTwoElements);
        return productList;
    }
    //Search product for adminPage
    public List<Product> searchProductAdminPage(String keyWord){
        return productDao.searchProductsAdminPage(keyWord);
    }

    public Product getProductByCode(String productCode) {
        Product product = productDao.findProductByCode(productCode);
        if(product != null){
            return product;
        }
        else{
            throw new CodeNotFoundException("No se ha podido encontrar el producto con este código: " + productCode +
                    ". Por favor, intente otra vez!");
        }
    }
    public Product getRealProductById(Integer id) {
        Product product = productDao.findProductById(id);
        if(product != null){
            return product;
        }
        else{
            throw new CodeNotFoundException("No se ha podido encontrar el producto con esta ID: " + id +
                    ". Por favor, intente otra vez!");
        }
    }

    public List<ProductStockDto> getProductWithLowStock(){
        try {
            return productStockMapper.mapToDtoList(productDao.getProductWithLowStock());
        }catch (Exception e){
            throw new StatisticException("Could not found products with low stock", e);
        }
    }
}