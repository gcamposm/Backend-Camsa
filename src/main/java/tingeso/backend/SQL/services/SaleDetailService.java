package tingeso.backend.SQL.services;

import tingeso.backend.SQL.dao.ProductDao;
import tingeso.backend.SQL.dao.SaleDao;
import tingeso.backend.SQL.dao.SaleDetailDao;
import tingeso.backend.SQL.dto.SaleDetailDto;
import tingeso.backend.SQL.dto.SaleDto;
import tingeso.backend.SQL.mappers.SaleDetailMapper;
import tingeso.backend.SQL.mappers.SaleMapper;
import tingeso.backend.SQL.models.Product;
import tingeso.backend.SQL.models.Sale;
import tingeso.backend.SQL.models.SaleDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@SuppressWarnings("Duplicates")
@Service
public class SaleDetailService {

    @Autowired
    private SaleDetailDao saleDetailDao;

    @Autowired
    private SaleDao saleDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private SaleDetailMapper saleDetailMapper;

    @Autowired
    private SaleMapper saleMapper;

    //CRUD
    //----------------------------------------------------------------
    //----------------------------------------------------------------
    public List<SaleDetailDto> getAllSaleDetail(){
        List<SaleDetail> saleDetailList = saleDetailDao.findAll();
        return saleDetailMapper.mapToDtoList(saleDetailList);
    }

    public SaleDetailDto getSaleDetailById(Integer id){
        if(saleDetailDao.findById(id).isPresent()){
            return saleDetailMapper.mapToDto(saleDetailDao.findSaleDetailById(id));
        }
        else{
            return  null;
        }
    }

    public void updateSaleDetail(SaleDetailDto saleDetailDto, Integer id){
        if(saleDetailDao.findById(id).isPresent()){
            SaleDetail saleDetailFinded = saleDetailDao.findSaleDetailById(id);
            saleDetailFinded.setQuantity(saleDetailDto.getQuantity());
            saleDetailFinded.setDiscount(saleDetailDto.getDiscount());
            saleDetailFinded.setSubtotal(saleDetailDto.getSubtotal());
            saleDetailFinded.setProduct(saleDetailDto.getProduct());
            saleDetailFinded.setSale(saleDetailDto.getSale());
            saleDetailDao.save(saleDetailFinded);
        }
    }

    public void deleteSaleDetail(Integer id){
        SaleDetail saleDetail = saleDetailDao.findSaleDetailById(id);
        saleDetailDao.delete(saleDetail);

    }

    public SaleDetailDto createSaleDetail(SaleDetailDto saleDetailDto){
        return saleDetailMapper.mapToDto(saleDetailDao.save(saleDetailMapper.mapToModel(saleDetailDto)));
    }
    //----------------------------------------------------------------
    //----------------------------------------------------------------
    //Quitar un producto de una venta (quitar su detalle de venta)
    public SaleDetailDto quitProduct(Integer saleId, Integer productId){
        Sale sale = saleDao.findSaleById(saleId);
        Product product = productDao.findProductById(productId);
        List<SaleDetail> saleDetailList = sale.getSaleDetailList();
        for (SaleDetail saleDetail: saleDetailList
             ) {
            if(saleDetail.getProduct().getId().equals(productId)){
                Integer quantity = saleDetail.getQuantity();
                product.setStock(product.getStock() + quantity);
                productDao.save(product);
                sale.setTotal(sale.getTotal().subtract(saleDetail.getSubtotal()));
                saleDetail.setQuantity(0);
                saleDetail.setSubtotal(BigDecimal.ZERO);
                saleDetail.setTotalWithDiscount(BigDecimal.ZERO);
                saleDetailDao.save(saleDetail);
                sumTotal(sale);
                sumQuantity(sale);
                sumDiscount(sale);
                sumTotalWithDiscount(sale);
                saleDao.save(sale);
                return saleDetailMapper.mapToDto(saleDetail);
            }
        }
        return null;
    }

    //Añade un producto a una venta y también su cantidad
    public SaleDto appendProductWithQuantity(Integer saleId, Product product, Integer quantity){
        Sale sale = saleDao.findSaleById(saleId);

        List<SaleDetail> saleDetailList = sale.getSaleDetailList();
        try{
            //Calcular total, subtotal y cantidad de productos
            SaleDetail saleDetail = findSaleDetail(saleDetailList, product);
            if(saleDetail != null) {
                saleDetail = addProductToSaleDetail(sale, saleDetail, product, quantity);
                return saleMapper.mapToDto(sale);
            }
            else{
                SaleDetail newSaleDetail = new SaleDetail();
                newSaleDetail.setProduct(product);
                newSaleDetail.setSale(sale);
                newSaleDetail.setProductName(product.getModuleName());
                addProductToSaleDetail(sale, newSaleDetail, product, quantity);
                sale.getSaleDetailList().add(newSaleDetail);
                System.out.println(sale.getSaleDetailList().size());
                return saleMapper.mapToDto(sale);
            }
        }catch (Exception e){
            return null;
        }
    }
    //Añade un producto comodín a una venta
    public SaleDto appendJokerProducts(Integer saleId, Integer productId,
                                       Integer quantity, int price,
                                       String productName){
        Product product = productDao.findProductById(productId);
        //Mantener el stock alto
        if(product != null) {
            if(isJokerProduct(product)) {
                //mantener stock alto
                product.setStock(1000000);
                productDao.save(product);
                //Agregar producto comodin
                SaleDto saleDto = appendProductWithQuantity(saleId, product, quantity);
                List<SaleDetail> saleDetailList = saleDto.getSaleDetailList();
                SaleDetail saleDetail = findSaleDetail(saleDetailList, product);
                if (saleDetail != null) {
                    saleDetail.setProductName(productName);
                    saleDetail.setUnitaryPrice(BigDecimal.valueOf(price));
                    saleDetail.calculateSubTotal();
                    saleDetail.calculateDiscount("Local");
                    saleDetailDao.save(saleDetail);
                    actualizeSale(saleDetail.getSale());
                    return saleMapper.mapToDto(saleDetail.getSale());
                }
            }
        }
        return null;
    }

    //verifica si el producto es del tipo comodin
    private Boolean isJokerProduct(Product product){
        return product.getCode().matches("[0-9]");
    }
    //Vincula un producto con un detalle de venta, actualizando cantidades
    private SaleDetail addProductToSaleDetail(Sale sale, SaleDetail saleDetail, Product product, int quantity){
        saleDetail.setQuantity(saleDetail.getQuantity() + quantity);
        //Si no es el producto comodin se calcula el precio unitario según el precio del producto
        if(!isJokerProduct(saleDetail.getProduct())){
            saleDetail.calculateUnitaryPrice("Local");
        }
        //Calcular los descuentos y precios unitarios
        saleDetail.calculateSubTotal();
        saleDetail.calculateDiscount("Local");
        //Guardar y actualizar
        product.setStock(product.getStock() - quantity);
        saleDetailDao.save(saleDetail);
        productDao.save(product);
        //Actualizar la venta de forma completa
        actualizeSale(sale);
        return saleDetail;
    }
    //Actualiza la venta y todos sus valores
    private void actualizeSale(Sale sale){
        sumTotal(sale);
        sumQuantity(sale);
        sumDiscount(sale);
        sumTotalWithDiscount(sale);
        saleDao.save(sale);
    }

    //Editar la cantidad de un producto asociado a un detalle de venta
    public SaleDto editQuantity(Integer saleId, Integer productId, Integer quantity){
        Sale sale = saleDao.findSaleById(saleId);
        Product product = productDao.findProductById(productId);
        List<SaleDetail> saleDetailList = sale.getSaleDetailList();
        Integer stock = product.getStock();
        Integer quantityToAdd = checkQuantity(saleDetailList, product, quantity);
        SaleDetail saleDetail = findSaleDetail(saleDetailList, product);

        if(quantityToAdd!= null && quantity >= 0 && saleDetail != null){
            if(quantityToAdd > 0 && stock >= quantityToAdd){
                saleDetail.setQuantity(quantity);
                saleDetail.sumItems(product, "Local");
                product.setStock(product.getStock() - quantityToAdd);
            }
            else if(quantityToAdd < 0){
                saleDetail.setQuantity(quantity);
                saleDetail.restItems(quantity, product, "Local");
                product.setStock(product.getStock() - quantityToAdd);
            }
            else if(quantityToAdd > 0){
                saleDetail.setQuantity(saleDetail.getQuantity() + product.getStock());
                saleDetail.sumItems(product, "Local");
                product.setStock(0);
            }
        }
        else{
            return saleMapper.mapToDto(saleDao.save(sale));
        }
        //CALCULATE DISCOUNT
        saleDetail.calculateUnitaryPrice("Local");
        saleDetail.calculateDiscount("Local");
        //SAVE
        saleDetailDao.save(saleDetail);
        productDao.save(product);
        sumTotal(sale);
        sumQuantity(sale);
        sumDiscount(sale);
        sumTotalWithDiscount(sale);
        return saleMapper.mapToDto(saleDao.save(sale));
    }
    //METODOS PARA ACTUALIZAR LA VENTA
    //-------------------------------------
    //-------------------------------------
    private void sumTotal(Sale sale){
        BigDecimal total = saleDetailDao.sumAllProductsBySaleId(sale);
        sale.setTotal(total);
    }

    private void sumQuantity(Sale sale){
        Integer totalQuantity = saleDetailDao.sumAllQuantity(sale);
        sale.setTotalQuantity(totalQuantity);
    }

    private void sumDiscount(Sale sale){
        BigDecimal totalDiscount = saleDetailDao.sumAllDiscount(sale);
        sale.setTotalDiscount(totalDiscount);
    }

    private void sumTotalWithDiscount(Sale sale){
        BigDecimal totalsWithDiscounts = saleDetailDao.sumAllTotalsWithDiscounts(sale);
        totalsWithDiscounts = totalsWithDiscounts.subtract(sale.getPositiveBalance());
        sale.setTotalWithDiscount(totalsWithDiscounts);
    }
    //-------------------------------------
    //-------------------------------------

    //Verifica si el detalle de venta existe y calcula la diferencia entre la cantidad ingresada y la que ya existe
    private Integer checkQuantity(List<SaleDetail> saleDetailList, Product product, Integer quantity){
        for (SaleDetail saleDetail: saleDetailList
        ) {
           if(saleDetail.getProduct().equals(product)){
               return quantity - saleDetail.getQuantity();
           }
        }
        return null;
    }
    //Busca un detalle de venta según su producto
    private SaleDetail findSaleDetail(List<SaleDetail> saleDetailList, Product product){
        for (SaleDetail saleDetail: saleDetailList
             ) {
            if(saleDetail.getProduct().equals(product)){
                return saleDetail;
            }
        }
        return null;
    }
    //Añade un descuento a un detalle de venta
    public SaleDetailDto addDiscount(Integer saleId, Integer productId, Integer discount) {
        BigDecimal bigdiscount = new BigDecimal(discount);
        Sale sale = saleDao.findSaleById(saleId);
        Product product = productDao.findProductById(productId);
        SaleDetail saleDetail = findSaleDetail(sale.getSaleDetailList(), product);
        if(discount <= 0){
            return saleDetailMapper.mapToDto(saleDetail);
        }
        saleDetail.calculateDiscount("Local");
        saleDetail.setDiscount(saleDetail.getDiscount().add(bigdiscount));
        saleDetail.actualizeDiscountMount();
        saleDetailDao.save(saleDetail);
        sumTotal(sale);
        sumDiscount(sale);
        sumTotalWithDiscount(sale);
        saleDao.save(sale);
        return saleDetailMapper.mapToDto(saleDetail);
    }

    //Busca todos los detalles de venta segun un id de venta
    public List<SaleDetailDto> findSaleDetailBySaleId(Integer saleId){
        Sale sale = saleDao.findSaleById(saleId);
        return saleDetailMapper.mapToDtoList(saleDetailDao.findSaleDetailBySale(sale));
    }
    // ---- Nuevos metodos para la venta ---










    //METODOS QUE QUIZAS NO SE USAN
    //------------------------------------------------------
    public SaleDto changeUnitaryPrice(int saleDetailId, int unitaryPrice){
        SaleDetail saleDetail = saleDetailDao.findSaleDetailById(saleDetailId);
        saleDetail.setUnitaryPrice(BigDecimal.valueOf(unitaryPrice));
        saleDetail.calculateSubTotal();
        saleDetail.calculateDiscount("Local");
        saleDetailDao.save(saleDetail);
        actualizeSale(saleDetail.getSale());
        return saleMapper.mapToDto(saleDetail.getSale());
    }
    public SaleDetailDto createSaleDetailWithProduct(Integer saleId, Integer productId){
        Sale sale = saleDao.findSaleById(saleId);
        Product product = productDao.findProductById(productId);
        List<SaleDetail> saleDetailList = sale.getSaleDetailList();
        for (SaleDetail saleDetail: saleDetailList
        ) {
            if(saleDetail.getProduct().getId().equals(productId)){
                saleDetail.setQuantity(saleDetail.getQuantity() + 1);
                BigDecimal quantity = BigDecimal.valueOf(saleDetail.getQuantity());
                saleDetail.setSubtotal(product.getPrice("Local").multiply(quantity));
                saleDetail.calculateUnitaryPrice("Local");
                saleDetail.calculateDiscount("Local");
                product.setStock(product.getStock() - 1);
                productDao.save(product);
                //findAndActualizeSale(saleId);
                return saleDetailMapper.mapToDto(saleDetailDao.save(saleDetail));
            }
        }
        SaleDetail saleDetail = new SaleDetail();
        //DESCONTAR STOCK
        product.setStock(product.getStock() - 1);
        productDao.save(product);
        //CREAR SALE DETAIL
        saleDetail.setSale(sale);
        saleDetail.setQuantity(1);
        saleDetail.setProduct(product);
        saleDetail.setSubtotal( product.getPrice("Local"));
        //DISCOUNT
        saleDetail.calculateUnitaryPrice("Local");
        saleDetail.calculateDiscount("Local");
        //findAndActualizeSale(saleId);
        return saleDetailMapper.mapToDto(saleDetailDao.save(saleDetail));
    }
    private void findAndActualizeSale(Integer saleId){
        Sale sale = saleDao.findSaleById(saleId);
        BigDecimal total = saleDetailDao.sumAllProductsBySaleId(sale);
        Integer totalQuantity = saleDetailDao.sumAllQuantity(sale);
        sale.setTotalDiscount(saleDetailDao.sumAllDiscount(sale));
        sale.setTotalWithDiscount(saleDetailDao.sumAllTotalsWithDiscounts(sale));
        sale.setTotal(total);
        sale.setTotalQuantity(totalQuantity);
        saleDao.save(sale);
    }
    public void quitProductAndDeleteSaleDetail(Integer saleId, Integer productId){
        SaleDetailDto saleDetailDto = quitProduct(saleId, productId);
        SaleDetail saleDetail = saleDetailDao.findSaleDetailById(saleDetailDto.getId());
        deleteSaleDetail(saleDetail.getId());
    }
}