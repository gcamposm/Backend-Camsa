package ventas.backend.SQL.services;


import tingeso.backend.Exceptions.*;
import tingeso.backend.SQL.dao.*;
import ventas.backend.Exceptions.*;
import ventas.backend.SQL.dao.*;
import ventas.backend.SQL.dto.ProductQuantity;
import ventas.backend.SQL.dto.ReducedSaleDetailDto;
import ventas.backend.SQL.dto.SaleInformationDto;
import ventas.backend.SQL.mappers.SaleDetailMapper;
import tingeso.backend.SQL.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ventas.backend.SQL.models.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("Duplicates")
@Service
public class ReducedSaleDetailService {
    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductService productService;

    @Autowired
    private SaleDetailDao saleDetailDao;

    @Autowired
    private SaleDetailMapper saleDetailMapper;

    @Autowired
    private SaleDao saleDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DeskDao deskDao;


    //Create a sale with information and sale details
    public Sale createSaleWithSaleDetails(SaleInformationDto saleInformationDto){
        ReducedSaleDetailDto[] reducedSaleDetailDtos = saleInformationDto.getReducedSaleDetailDtos();
        Integer userId = saleInformationDto.getUserId();
        Integer deskId = saleInformationDto.getDeskId();
        Integer saleId = saleInformationDto.getSaleId();
        Boolean finalized = saleInformationDto.getFinalized();
        String description = saleInformationDto.getDescription();
        BigDecimal positiveBalance = saleInformationDto.getPositiveBalance();

        Sale sale;
        //Create new Sale
        if(saleId == null) {
            sale = createSale(userId, deskId, finalized, description, positiveBalance);
        }
        //Edit the sale if exist
        else{
            sale = saleDao.findSaleById(saleId);
            sale.setDescription(saleInformationDto.getDescription());
            if(sale == null) throw new IdNotFoundException("Could not found the sale with id: " + saleId);
            List<SaleDetail> saleDetailList = sale.getSaleDetailList();

            //delete the sale details in the database
            deleteSaleDetailsBySale(sale);
            //delete the array
            sale.deleteAllSaleDetails();
        }
        //make all sale details for sale
        //can throw exceptions
        for (ReducedSaleDetailDto reducedSaleDetailDto : reducedSaleDetailDtos
                ) {
            createSaleDetailByReducedSaleDetail(reducedSaleDetailDto, sale);
        }
        //Calculate Promotions
        sale = calculatePromotions(sale);


        //can throw exception
        //actualizing sale totals and quantities
        actualizeSale(sale);
        return saleDao.save(sale);
    }


    private List<ReducedSaleDetailDto> calculalteStockDifference(List<SaleDetail> saleDetailList,
                                                                 ReducedSaleDetailDto[] reducedSaleDetailDtos){
        List<ReducedSaleDetailDto> reducedSaleDetailDtoList = Arrays.asList(reducedSaleDetailDtos);
        List<ReducedSaleDetailDto> reducedSaleDetailDtoListFromSaleDetails = saleDetailMapper.mapToReducedSaleDetailDtoList(saleDetailList);
        return reducedSaleDetailDtoList;
    }

    //delete all sale details in a sale
    private void deleteSaleDetailsBySale(Sale sale){
        List<SaleDetail> saleDetailList = sale.getSaleDetailList();
        for (SaleDetail saleDetail: saleDetailList
             ) {
            saleDetailDao.deleteSaleDetail(saleDetail.getId());
        }
    }

    private Boolean isJokerProduct(Product product){
        return product.getCode().matches("[0-9]");
    }
    //Create a sale detail using a sale and a reduced sale detail
    private void createSaleDetailByReducedSaleDetail(ReducedSaleDetailDto reducedSaleDetailDto, Sale sale){
        SaleDetail saleDetail = new SaleDetail();
        //can throw exceptions

        Product product = productService.getRealProductById(reducedSaleDetailDto.getProductId());
        //discountSingleProductStock(product, reducedSaleDetailDto.getQuantity());

        try {
            saleDetail.setProduct(product);
            if(isJokerProduct(product)){
                saleDetail.setProductName(reducedSaleDetailDto.getName());
            }
            else {
                saleDetail.setProductName(product.getModuleName());
            }
            saleDetail.setUnitaryPrice(reducedSaleDetailDto.getUnitaryPrice());
            saleDetail.setSale(sale);
            saleDetail.setDiscount(reducedSaleDetailDto.getDiscount());
            saleDetail.setQuantity(reducedSaleDetailDto.getQuantity());
            saleDetail.setSubtotal(reducedSaleDetailDto.getSubTotal());
            saleDetail.setTotalWithDiscount(reducedSaleDetailDto.getSubTotalWithDiscount());
            sale.getSaleDetailList().add(saleDetailDao.save(saleDetail));
        }catch (Exception e){
            throw new CreateSaleDetailException("No se ha podido crear el detalle de venta: ", e);
        }
    }

    private Sale calculatePromotions(Sale sale){
        try {
            calculateProductsDiscounts(sale);
        }catch (Exception e){
            throw new CalculatePromotionException("Hubo un problema al calcular las promociones para la venta: " + sale.getTicketNumber());
        }
        return sale;
    }

    // calculate the product discount of a sale
    private void calculateProductsDiscounts(Sale sale) {
        List<SaleDetail> saleDetailList = sale.getSaleDetailList();
        for (SaleDetail saleDetail: saleDetailList
             ) {
            BigDecimal productDiscount = saleDetail.calculateProductDiscount("Local");
            if(productDiscount.compareTo(BigDecimal.ZERO) > 0){
                saleDetail.setDiscount(saleDetail.getDiscount().add(productDiscount));
                saleDetail.setTotalWithDiscount(saleDetail.getTotalWithDiscount().subtract(productDiscount));
            }
            saleDetailDao.save(saleDetail);
        }
    }

    //Make a discount of the product stock, using local, warehouse or event
    private ProductQuantity discountSingleProductStock(Product product, int quantity){
        Integer local_stock = product.getStock();
        Integer warehouse_stock = product.getWarehouseStock();
        Integer event_stock = product.getEventStock();
        if(local_stock == null){ local_stock = 0;} if(warehouse_stock == null){warehouse_stock = 0;} if(event_stock == null){event_stock = 0;}


        if(local_stock >= quantity){
            product.setStock(local_stock - quantity);
            productDao.save(product);
            return new ProductQuantity(product, quantity , 0, 0);
        }
        else if(local_stock + warehouse_stock >= quantity){
            //difference is negative
            int difference = quantity - local_stock;
            product.setStock(0);
            product.setWarehouseStock(warehouse_stock - difference);
            productDao.save(product);
            return new ProductQuantity(product, local_stock, difference, 0);
        }
        else if(local_stock + warehouse_stock + event_stock >= quantity){
            //EJ quantity = 24, local_stock = 5 => difference1 = 19
            int difference1 = quantity - local_stock;
            //EJ difference1 = 19, warehouse_stock = 10 => difference2 = 9
            int difference2 = difference1 - warehouse_stock;
            product.setStock(0);
            product.setWarehouseStock(0);
            //EJ difference2 = 9, event_stock = 10 => new event_stock = 1
            product.setEventStock(event_stock - difference2);
            productDao.save(product);
            return new ProductQuantity(product, local_stock, warehouse_stock, difference2);
        }
        else{
            System.out.println("no hay stock");
            throw new NotSufficientStockException("No hay stock suficiente para el producto con código: " + product.getCode());
        }
    }

    //Make the product discount of all product in a sale
    private void discountMultipleProducts(Sale sale){
        List<SaleDetail> saleDetailList = sale.getSaleDetailList();
        List<ProductQuantity> productQuantityList = new ArrayList<>();
        try {
            for (SaleDetail saleDetail : saleDetailList) {
                ProductQuantity productQuantity = discountSingleProductStock(saleDetail.getProduct(), saleDetail.getQuantity());
                System.out.println(productQuantity);
                productQuantityList.add(productQuantity);
            }
        }catch (Exception e){
            System.out.println("Refill products");
            for (ProductQuantity productQuantity: productQuantityList
                 ) {
                refillProductStock(productQuantity);
            }
            throw new RuntimeException(e);
        }
    }
    //Refill product stock
    private void refillProductStock(ProductQuantity productQuantity){
        Product product = productQuantity.getProduct();

        Integer local_stock = product.getStock();
        Integer event_stock = product.getEventStock();
        Integer warehouse_stock = product.getWarehouseStock();
        if(local_stock == null){ local_stock = 0;} if(warehouse_stock == null){warehouse_stock = 0;} if(event_stock == null){event_stock = 0;}
        System.out.println("Refill PQ: " + productQuantity.toString());
        product.setStock(local_stock + productQuantity.getLocalQuantity());
        product.setEventStock(event_stock + productQuantity.getEventQuantity());
        product.setWarehouseStock(warehouse_stock + productQuantity.getWarehouseQuantity());
        productDao.save(product);
        System.out.println("Product: " + product.toString());
    }


    //METODOS PARA ACTUALIZAR LA VENTA
    //-------------------------------------
    //-------------------------------------
    //sum total
    private void sumTotal(Sale sale){
        BigDecimal total = saleDetailDao.sumAllProductsBySaleId(sale);
        sale.setTotal(total);
    }

    //sum all quantities
    private void sumQuantity(Sale sale){
        Integer totalQuantity = saleDetailDao.sumAllQuantity(sale);
        sale.setTotalQuantity(totalQuantity);
    }

    //sum all discounts
    private void sumDiscount(Sale sale){
        BigDecimal totalDiscount = saleDetailDao.sumAllDiscount(sale);
        sale.setTotalDiscount(totalDiscount);
    }

    //sum totals with discounts
    private void sumTotalWithDiscount(Sale sale){
        BigDecimal totalsWithDiscounts = saleDetailDao.sumAllTotalsWithDiscounts(sale);
        totalsWithDiscounts = totalsWithDiscounts.subtract(sale.getPositiveBalance());
        sale.setTotalWithDiscount(totalsWithDiscounts);
    }

    //actualize all sale fields
    private void actualizeSale(Sale sale){
        try {
            sumTotal(sale);
            sumQuantity(sale);
            sumDiscount(sale);
            sumTotalWithDiscount(sale);
            saleDao.save(sale);
        }catch (Exception e){
            throw new CalculateSaleTotalsException("No se ha podido procesar la venta Nº: " + sale.getTicketNumber(), e);
        }
    }

    //create a sale using id, deskid, finalized, and description
    public Sale createSale(Integer userId, Integer deskId, Boolean finalized, String description, BigDecimal positiveBalance) {
        try {
            LocalDateTime localDateTime = LocalDateTime.now();
            Desk desk = deskDao.findDeskById(deskId);
            User user = userDao.findById(userId);
            Sale sale = new Sale();
            sale.setSaleDetailList(new ArrayList<>());
            Sale lastSale = saleDao.findTopByOrderByIdDesc();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formatDateTime = localDateTime.format(formatter);
            BigDecimal zero = new BigDecimal("0");
            if (lastSale != null) {
                Integer ticketNumber = lastSale.getTicketNumber();
                System.out.println(ticketNumber);
                sale.setTicketNumber(ticketNumber + 1);
            } else {
                sale.setTicketNumber(1);
            }
            sale.setDesk(desk);
            sale.setFormatDate(formatDateTime);
            sale.setDate(localDateTime);
            sale.setTotal(zero);
            sale.setDescription(description);
            sale.setFinalized(finalized);
            sale.setTotalDiscount(zero);
            sale.setTotalQuantity(0);
            sale.setTotalWithDiscount(zero);
            sale.setUserventas(user);
            sale.setPositiveBalance(positiveBalance);
            return saleDao.save(sale);
        }catch (Exception e){
            throw new CreateSaleException("No se ha podido crear la venta con el usuario: " + userId + ", y caja: "+ deskId+", Error: ",e);
        }
    }

    public Sale finalizeSale(Integer id){
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formatDateTime = localDateTime.format(formatter);
        Sale sale = saleDao.findSaleById(id);
        sale.setFinalized(true);
        sale.setDate(localDateTime);
        sale.setFormatDate(formatDateTime);
        //throw an error before the sale was saved
        discountMultipleProducts(sale);
        saleDao.save(sale);
        return sale;
    }


}
