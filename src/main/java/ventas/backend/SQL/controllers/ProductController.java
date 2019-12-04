package ventas.backend.SQL.controllers;

import ventas.backend.FileManagement.service.FileStorageService;
import ventas.backend.SQL.dao.ImageDao;
import ventas.backend.SQL.dao.ProductDao;
import ventas.backend.SQL.dto.OneProductDto;
import ventas.backend.SQL.dto.ProductInterfaceDto;
import ventas.backend.SQL.models.Category;
import ventas.backend.SQL.models.Image;
import ventas.backend.SQL.models.Product;
import ventas.backend.SQL.models.SubCategory;
import ventas.backend.SQL.services.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ServletContext context;

    @Autowired
    private ProductService productService;


    @Autowired
    private ProductDao productDao;

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private FileStorageService fileStorageService;



    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<List<ProductInterfaceDto>> getAllProducts(){
        try{
            return ResponseEntity.ok(productService.getAllProduct());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/web/")
    @ResponseBody
    public ResponseEntity<List<ProductInterfaceDto>> getAllProductsForWeb(){
        try{
            return ResponseEntity.ok(productService.getAllProduct());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/{id}")
    //@ResponseBody
    public ResponseEntity<ProductInterfaceDto> findProductById (@PathVariable("id") Integer id){

        try{
            return ResponseEntity.ok(productService.getProductByID(id));
        }

        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/web/{id}")
    //@ResponseBody
    public ResponseEntity<ProductInterfaceDto> findProductByIdForWeb (@PathVariable("id") Integer id){

        try{
            return ResponseEntity.ok(productService.getProductByID(id));
        }

        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity create (@RequestBody OneProductDto oneProductDto){

        try{
            if(productDao.findProductByCode(oneProductDto.getCode()) != null)
            {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El código del producto ingresado ya existe.");
            }
            return ResponseEntity.ok(productService.createProduct(oneProductDto));
        }

        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/code/{code}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<Boolean> create (@PathVariable String code){

        try{
            Product product = productDao.findProductByCode(code);
            if(product != null){
                return ResponseEntity.ok(true);
            }
            return ResponseEntity.ok(false);
        }

        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity update (@PathVariable("id") Integer id, @RequestBody OneProductDto oneProductDto){

        try{
            return ResponseEntity.ok(productService.updateProduct(oneProductDto, id));
        }

        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity delete (@PathVariable Integer id){

        try{
            return ResponseEntity.ok(productService.deleteProduct(id));
        }

        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(value = "{id}/uploadImage", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadImage(@PathVariable Integer id, @RequestParam("file") MultipartFile file) throws IOException {
        Product productToUpdate = productDao.findProductById(id);
        if (productToUpdate == null)
            return new ResponseEntity<>("El producto no se ha podido encontrar.", HttpStatus.BAD_REQUEST);
        //return ResponseEntity.ok(convertFile.getAbsolutePath());
        return ResponseEntity.ok(productDao.save(productService.uploadImage(productToUpdate, file.getOriginalFilename(), file.getBytes())));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(value = "{id}/changePrincipalImage", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity changePrincipalImage(@PathVariable Integer id, @RequestParam("file") MultipartFile file) throws IOException {
        Product productToUpdate = productDao.findProductById(id);
        if (productToUpdate == null)
            return new ResponseEntity<>("El producto no se ha podido encontrar.", HttpStatus.BAD_REQUEST);
        else
        {
            Image principalImage = imageDao.findImageByProductAndPrincipal(productToUpdate, true);
            if(principalImage != null)
            {
                principalImage.setPrincipal(false);
                imageDao.delete(principalImage);
            }
            productToUpdate = productService.uploadImage(productToUpdate, file.getOriginalFilename(), file.getBytes());
            productToUpdate.getImageList().get(productToUpdate.getImageList().size()-1).setPrincipal(true);
            return ResponseEntity.ok(productDao.save(productToUpdate));
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping(value = "{id}/getprincipalimage", method = RequestMethod.GET)
    public byte[] getprincipalimage(@PathVariable Integer id, HttpServletRequest request) {
        //String relativeWebPath = "WEB-INF/classes/static/imagenes";
        //String absoluteFilePath = context.getRealPath(relativeWebPath);
        //String absoluteFilePath =  "src/main/resources/static/imagenes/";
        Path absoluteFilePath = fileStorageService.getFileStorageLocation();
        Product productToUpdate = productDao.findProductById(id);
        if (productToUpdate != null) {
            try {
                Image principalImage = imageDao.findImageById(imageDao.findImageByPrincipalEquals(productToUpdate));
                if(principalImage == null){
                    Path path = Paths.get(absoluteFilePath + "/nodisponible.png");
                    byte[] data = Files.readAllBytes(path);
                    return data;
                }
                String rpath =  absoluteFilePath + "/" + principalImage.getName() + principalImage.getExtension(); // whatever path you used for storing the file
                Path path = Paths.get(rpath);
                byte[] data = Files.readAllBytes(path);
                return data;
            } catch (IOException x) {
                System.err.format("IOException: %s%n", x);
                byte[] data = {0};
                return data;
            }
        }else {
            byte[] data = {0};
            return data;
        }
    }

    @RequestMapping(value = "web/{id}/getprincipalimage", method = RequestMethod.GET)
    public byte[] getprincipalimageForWeb(@PathVariable Integer id, HttpServletRequest request) {
        //String relativeWebPath = "WEB-INF/classes/static/imagenes";
        //String absoluteFilePath = context.getRealPath(relativeWebPath);
        //String absoluteFilePath =  "src/main/resources/static/imagenes/";
        Path absoluteFilePath = fileStorageService.getFileStorageLocation();
        Product productToUpdate = productDao.findProductById(id);
        if (productToUpdate != null) {
            try {
                Image principalImage = imageDao.findImageById(imageDao.findImageByPrincipalEquals(productToUpdate));
                if(principalImage == null){
                    Path path = Paths.get(absoluteFilePath + "/nodisponible.png");
                    byte[] data = Files.readAllBytes(path);
                    return data;
                }
                String rpath =  absoluteFilePath + "/" + principalImage.getName() + principalImage.getExtension(); // whatever path you used for storing the file
                Path path = Paths.get(rpath);
                byte[] data = Files.readAllBytes(path);
                return data;
            } catch (IOException x) {
                System.err.format("IOException: %s%n", x);
                byte[] data = {0};
                return data;
            }
        }else {
            byte[] data = {0};
            return data;
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping(value = "{id}/allImages", method = RequestMethod.GET)
    public List<byte[]> allimagesbyproduct(@PathVariable Integer id, HttpServletRequest request) {

        Product product = productDao.findProductById(id);
        List<byte[]> images = new ArrayList<>();
        //String relativeWebPath = "WEB-INF/classes/static/imagenes";
        //String absoluteFilePath = context.getRealPath(relativeWebPath);
        Path absoluteFilePath = fileStorageService.getFileStorageLocation();
        if (product != null) {
            try {
                for (Image image: product.getImageList()
                ) {
                    String rpath =  absoluteFilePath+ "/" + image.getName() + image.getExtension(); // whatever path you used for storing the file
                    Path path = Paths.get(rpath);
                    byte[] data = Files.readAllBytes(path);
                    images.add(data);
                }
                return images;
            } catch (IOException x) {
                System.err.format("IOException: %s%n", x);
                byte[] data = {0};
                images.add(data);
                return images;
            }
        } else {
            byte[] data = {0};
            images.add(data);
            return images;
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(value = "/deleteImage", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity deleteImage (@RequestParam("productId") Integer productId , @RequestParam("position") Integer position){
        //String relativeWebPath = "WEB-INF/classes/static/imagenes";
        //String absoluteFilePath = context.getRealPath(relativeWebPath);
        //String absoluteFilePath =  "src/main/resources/static/imagenes/";
        Path absoluteFilePath = fileStorageService.getFileStorageLocation();
        Product productToUpdate = productDao.findProductById(productId);
        if (productToUpdate != null) {
            Image principalImage = productToUpdate.getImageList().get(position);
            if (principalImage != null) {
                String rpath = absoluteFilePath + "/" + principalImage.getName() + principalImage.getExtension();
                File file = new File(rpath);
                imageDao.deleteImage(principalImage.getId());
                return ResponseEntity.ok(file.delete());
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(value = "/jokerProduct", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity jokerProduct (@RequestParam("price") BigDecimal price){
        if (price.intValue() < 0) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("No debe ingresar un precio menor a cero.");
        }
        return ResponseEntity.ok(productService.getJokerProduct(price));
    }

    @GetMapping("web/subCategory/{id}")
    @ResponseBody
    public ResponseEntity getProductsBySubCategoryId (@PathVariable("id") Integer id){
        try{
            return ResponseEntity.ok(productService.getProductsBySubCategoryId(id));
        } catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("web/random/{id}")
    @ResponseBody
    public ResponseEntity getProductsRandom (@PathVariable("id") Integer id){
        try{
            if(productService.getProductsRandom(id) == null)
            {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("No hay suficientes productos.");
            }
            return ResponseEntity.ok(productService.getProductsRandom(id));
        } catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("web/category/{id}")
    @ResponseBody
    public ResponseEntity getProductByCategoryId (@PathVariable("id") Integer id){
        try{
            return ResponseEntity.ok(productService.getProductsByCategoryId(id));
        } catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "web/filter", method = RequestMethod.GET, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity filterForWeb(@RequestParam("name") String name,
                                               @RequestParam("minPrice") Integer minPrice,
                                               @RequestParam("maxPrice") Integer maxPrice,
                                               @RequestParam("category") Category category,
                                               @RequestParam("subCategory") SubCategory subCategory,
                                               @RequestParam("productList") List<Product> productList) throws IOException {
        try {
            return ResponseEntity.ok(productService.filterForWeb(name, minPrice, maxPrice, category, subCategory, productList));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "web/order", method = RequestMethod.GET, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity orderForWeb(@RequestParam("productList") List<Product> productList,
                                      @RequestParam("order") String order) throws IOException {
        try {
            return ResponseEntity.ok(productService.orderForWeb(order, productList));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    //PAGINATION FILTERS
    //---------------------------
    @GetMapping("web/pagination/{from}/{to}")
    @ResponseBody
    public ResponseEntity getProductByPagination (@PathVariable("from") Integer from, @PathVariable("to") Integer to){
        try{
            return ResponseEntity.ok(productService.getProductByPagination(from, to));
        } catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @RequestMapping(value = "/searchProductAdminPage", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity searchProductAdminPAge (@RequestParam("keyWord") String keyWord){
        try{
            String cleanKeyWord = StringUtils.stripAccents(keyWord);
            return ResponseEntity.ok(productService.searchProductAdminPage(cleanKeyWord));

        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getCause());
        }
    }
}