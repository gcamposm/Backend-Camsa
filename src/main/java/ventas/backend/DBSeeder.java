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
import java.util.ArrayList;
import java.util.List;

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

    public void firstSeeder(){
        List<PaymentMethod> paymentMethodList = paymentMethodDao.findAll();
        List<PhysicalDesk> physicalDeskList = physicalDeskDao.findAll();
        if(paymentMethodList.size() == 0){
            PaymentMethod paymentMethod1 = new PaymentMethod();
            paymentMethod1.setType("Crédito");
            paymentMethodDao.save(paymentMethod1);
            PaymentMethod paymentMethod2 = new PaymentMethod();
            paymentMethod2.setType("Débito");
            paymentMethodDao.save(paymentMethod2);
            PaymentMethod paymentMethod3 = new PaymentMethod();
            paymentMethod3.setType("Efectivo");
            paymentMethodDao.save(paymentMethod3);
            PaymentMethod paymentMethod4 = new PaymentMethod();
            paymentMethod4.setType("Cheques");
            paymentMethodDao.save(paymentMethod4);
            PaymentMethod paymentMethod5 = new PaymentMethod();
            paymentMethod5.setType("Transferencia bancaria");
            paymentMethodDao.save(paymentMethod5);
        }
        if(physicalDeskList.size() == 0)
        {
            PhysicalDesk physicalDesk = new PhysicalDesk();
            physicalDesk.setOffice("Tobalaba");
            physicalDesk.setDescription("1");
            physicalDesk.setDeskList(new ArrayList<>());
            physicalDeskDao.save(physicalDesk);
            PhysicalDesk physicalDesk2 = new PhysicalDesk();
            physicalDesk2.setOffice("Local 2");
            physicalDesk2.setDescription("2");
            physicalDesk2.setDeskList(new ArrayList<>());
            physicalDeskDao.save(physicalDesk2);
        }
        if(productDao.findProductByCode("0") == null){
            Integer i;
            for(i = 0; i < 10 ; i ++) {
                Product product = new Product();
                product.setCode(i.toString());
                product.setPriceLocal(BigDecimal.ZERO);
                product.setPriceWeb(BigDecimal.ZERO);
                product.setSize("S");
                product.setStock(1000000);
                product.setDeleted(false);
                product.setPublished(false);
                product.setModuleName("comodin" +i.toString());
                product.setWebName("");
                product.setCost(BigDecimal.ZERO);
                product.setWarehouseStock(1000000);
                product.setEventStock(1000000);
                product.setWebDiscount(0);
                product.setLocalDiscount(0);
                productDao.save(product);
            }
        }
    }

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
        heladoSimple.setWebDiscount(0);
        heladoSimple.setLocalDiscount(0);
        heladoSimple.setSubCategory(helados);
        heladoSimple.setDescription("/img/helado1.jpg");
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
        heladoDoble.setWebDiscount(0);
        heladoDoble.setLocalDiscount(0);
        heladoDoble.setSubCategory(helados);
        heladoDoble.setDescription("/img/helado2.jpg");
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
        heladotriple.setWebDiscount(0);
        heladotriple.setLocalDiscount(0);
        heladotriple.setSubCategory(helados);
        heladotriple.setDescription("/img/helado3.jpg");
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
        heladoQuadruple.setWebDiscount(0);
        heladoQuadruple.setLocalDiscount(0);
        heladoQuadruple.setSubCategory(helados);
        heladoQuadruple.setDescription("/img/helado4.jpg");
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
        moteMediano.setWebDiscount(0);
        moteMediano.setLocalDiscount(0);
        moteMediano.setSubCategory(motes);
        moteMediano.setDescription("/img/mote.png");
        productDao.save(moteMediano);
        Product moteGrande = new Product();
        moteGrande.setCode("");
        moteGrande.setPriceLocal(BigDecimal.valueOf(1000));
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
        moteGrande.setWebDiscount(0);
        moteGrande.setLocalDiscount(0);
        moteGrande.setSubCategory(motes);
        moteGrande.setDescription("/img/mote.png");
        productDao.save(moteGrande);
        Product moteGigante = new Product();
        moteGigante.setCode("");
        moteGigante.setPriceLocal(BigDecimal.valueOf(2000));
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
        moteGigante.setWebDiscount(0);
        moteGigante.setLocalDiscount(0);
        moteGigante.setSubCategory(motes);
        moteGigante.setDescription("/img/mote.png");
        productDao.save(moteGigante);
    }

    @Override
    public void run(String... args) throws Exception {
        firstSeeder();
        categorySeeder();
        subCategorySeeder();
        productSeeder();
    }
}
