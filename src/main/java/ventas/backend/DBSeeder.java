package ventas.backend;

import ventas.backend.SQL.dao.*;
import ventas.backend.SQL.dao.*;
import ventas.backend.SQL.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ventas.backend.SQL.models.Category;
import ventas.backend.SQL.models.Product;
import ventas.backend.SQL.models.SubCategory;

import java.math.BigDecimal;

@Component
@Order(2)
public class DBSeeder implements CommandLineRunner {

    @Autowired
    private PaymentMethodDao paymentMethodDao;

    @Autowired
    private DeskDao deskDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private SubCategoryDao subCategoryDao;

    @Autowired
    private PhysicalDeskDao physicalDeskDao;

    public void categorySeeder(){
        Category iceCream = new Category();
        iceCream.setName("Helados");
        iceCream.setDeleted(false);
        iceCream.setPublished(true);
        iceCream.setSubCategoryList(null);
        categoryDao.save(iceCream);
        Category mote = new Category();
        mote.setName("Motes");
        mote.setDeleted(false);
        mote.setPublished(true);
        mote.setSubCategoryList(null);
        categoryDao.save(mote);
    }
    public void subCategorySeeder(){
        Category helados = categoryDao.findByName("Helados");
        Category motes = categoryDao.findByName("Motes");
        SubCategory iceCream = new SubCategory();
        iceCream.setName("Helados");
        iceCream.setDeleted(false);
        iceCream.setPublished(true);
        iceCream.setCategory(helados);
        subCategoryDao.save(iceCream);
        SubCategory mote = new SubCategory();
        mote.setName("Motes");
        mote.setDeleted(false);
        mote.setPublished(true);
        mote.setCategory(motes);
        subCategoryDao.save(mote);
    }
    public void productSeeder(){
        SubCategory helados = subCategoryDao.findSubCategoriesByName("Helados");
        SubCategory motes = subCategoryDao.findSubCategoriesByName("Motes");
        Product heladoSimple = new Product();
        heladoSimple.setCode("");
        heladoSimple.setPriceLocal(BigDecimal.valueOf(400));
        heladoSimple.setPriceWeb(null);
        heladoSimple.setSize(null);
        heladoSimple.setStock(null);
        heladoSimple.setDeleted(false);
        heladoSimple.setPublished(true);
        heladoSimple.setModuleName("Helado Simple");
        heladoSimple.setWebName(null);
        heladoSimple.setCost(null);
        heladoSimple.setWarehouseStock(null);
        heladoSimple.setEventStock(null);
        heladoSimple.setWebDiscount(null);
        heladoSimple.setLocalDiscount(null);
        heladoSimple.setSubCategory(helados);
        productDao.save(heladoSimple);
        Product heladoDoble = new Product();
        heladoDoble.setCode("");
        heladoDoble.setPriceLocal(BigDecimal.valueOf(700));
        heladoDoble.setPriceWeb(null);
        heladoDoble.setSize(null);
        heladoDoble.setStock(null);
        heladoDoble.setDeleted(false);
        heladoDoble.setPublished(true);
        heladoDoble.setModuleName("Helado Doble");
        heladoDoble.setWebName(null);
        heladoDoble.setCost(null);
        heladoDoble.setWarehouseStock(null);
        heladoDoble.setEventStock(null);
        heladoDoble.setWebDiscount(null);
        heladoDoble.setLocalDiscount(null);
        heladoDoble.setSubCategory(helados);
        productDao.save(heladoDoble);
        Product heladotriple = new Product();
        heladotriple.setCode("");
        heladotriple.setPriceLocal(BigDecimal.valueOf(1000));
        heladotriple.setPriceWeb(null);
        heladotriple.setSize(null);
        heladotriple.setStock(null);
        heladotriple.setDeleted(false);
        heladotriple.setPublished(true);
        heladotriple.setModuleName("Helado Triple");
        heladotriple.setWebName(null);
        heladotriple.setCost(null);
        heladotriple.setWarehouseStock(null);
        heladotriple.setEventStock(null);
        heladotriple.setWebDiscount(null);
        heladotriple.setLocalDiscount(null);
        heladotriple.setSubCategory(helados);
        productDao.save(heladotriple);
        Product heladoQuadruple = new Product();
        heladoQuadruple.setCode("");
        heladoQuadruple.setPriceLocal(BigDecimal.valueOf(1400));
        heladoQuadruple.setPriceWeb(null);
        heladoQuadruple.setSize(null);
        heladoQuadruple.setStock(null);
        heladoQuadruple.setDeleted(false);
        heladoQuadruple.setPublished(true);
        heladoQuadruple.setModuleName("Helado Gigante");
        heladoQuadruple.setWebName(null);
        heladoQuadruple.setCost(null);
        heladoQuadruple.setWarehouseStock(null);
        heladoQuadruple.setEventStock(null);
        heladoQuadruple.setWebDiscount(null);
        heladoQuadruple.setLocalDiscount(null);
        heladoQuadruple.setSubCategory(helados);
        productDao.save(heladoQuadruple);
        Product moteMediano = new Product();
        moteMediano.setCode("");
        moteMediano.setPriceLocal(BigDecimal.valueOf(800));
        moteMediano.setPriceWeb(null);
        moteMediano.setSize(null);
        moteMediano.setStock(null);
        moteMediano.setDeleted(false);
        moteMediano.setPublished(true);
        moteMediano.setModuleName("Mote Mediano");
        moteMediano.setWebName(null);
        moteMediano.setCost(null);
        moteMediano.setWarehouseStock(null);
        moteMediano.setEventStock(null);
        moteMediano.setWebDiscount(null);
        moteMediano.setLocalDiscount(null);
        moteMediano.setSubCategory(motes);
        productDao.save(moteMediano);
        Product moteGrande = new Product();
        moteGrande.setCode("");
        moteGrande.setPriceLocal(BigDecimal.valueOf(700));
        moteGrande.setPriceWeb(null);
        moteGrande.setSize(null);
        moteGrande.setStock(null);
        moteGrande.setDeleted(false);
        moteGrande.setPublished(true);
        moteGrande.setModuleName("Mote Grande");
        moteGrande.setWebName(null);
        moteGrande.setCost(null);
        moteGrande.setWarehouseStock(null);
        moteGrande.setEventStock(null);
        moteGrande.setWebDiscount(null);
        moteGrande.setLocalDiscount(null);
        moteGrande.setSubCategory(motes);
        productDao.save(moteGrande);
        Product moteGigante = new Product();
        moteGigante.setCode("");
        moteGigante.setPriceLocal(BigDecimal.valueOf(700));
        moteGigante.setPriceWeb(null);
        moteGigante.setSize(null);
        moteGigante.setStock(null);
        moteGigante.setDeleted(false);
        moteGigante.setPublished(true);
        moteGigante.setModuleName("Mote Gigante");
        moteGigante.setWebName(null);
        moteGigante.setCost(null);
        moteGigante.setWarehouseStock(null);
        moteGigante.setEventStock(null);
        moteGigante.setWebDiscount(null);
        moteGigante.setLocalDiscount(null);
        moteGigante.setSubCategory(motes);
        productDao.save(moteGigante);
    }

    @Override
    public void run(String... args) throws Exception {
        categorySeeder();
        subCategorySeeder();
        productSeeder();
    }
}
