package tingeso.backend.SQL.services;

import tingeso.backend.Helpers.DateHelper;
import tingeso.backend.SQL.dao.SaleDao;
import tingeso.backend.SQL.dto.ProductSoldDto;
import tingeso.backend.SQL.dto.ProductSoldPerDayDto;
import tingeso.backend.SQL.dto.SaleDayStat;
import tingeso.backend.SQL.models.*;
import tingeso.backend.SQL.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class StatsService {
    @Autowired
    private SaleDao saleDao;

    Integer array[] = {0,1,2,3,4,5,6,7,8,9};
    List<Integer> comodinIdList = Arrays.asList(array);

    public List<SaleDayStat> getStats(Date firstDate, Date secondDate){
        List<LocalDateTime> localDateTimeList = DateHelper.intervalDateToList(firstDate, secondDate, 0);
        List<SaleDayStat> saleDayStatList = new ArrayList<>();
        for (LocalDateTime localDateTime: localDateTimeList
        ) {
            SaleDayStat saleDayStat = getStat(localDateTime);
            saleDayStatList.add(saleDayStat);

        }
        return saleDayStatList;
    }
    private SaleDayStat getStat(LocalDateTime date){

        LocalDateTime datePlus1Day = date.plusDays(1);
        System.out.println("Date: " + date);
        System.out.println("Date + 1: " + datePlus1Day);
        List<Sale> saleList = saleDao.findSaleByDateBetween(date, datePlus1Day);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formatDateTime = date.format(formatter);
        System.out.println("---------------------------");
        SaleDayStat saleDayStat = getTotalByDay(saleList);
        saleDayStat.setDay(date);
        saleDayStat.setFormattedDate(formatDateTime);
        System.out.println(saleDayStat);
        System.out.println("---------------------------");
        return saleDayStat;

    }
    private SaleDayStat getTotalByDay(List<Sale> saleList){
        Integer credito = 0;
        Integer debito = 0;
        Integer efectivo = 0;
        Integer cheques = 0;
        Integer transfer = 0;
        for (Sale sale: saleList
        ) {
            if(sale.getFinalized()) {
                for (SalePaymentMethod salePaymentMethod : sale.getSalePaymentMethodList()
                ) {
                    PaymentMethod paymentMethod = salePaymentMethod.getPaymentMethod();
                    switch (paymentMethod.getType()) {
                        case "Crédito":
                            credito = credito + salePaymentMethod.getAmount();
                            break;
                        case "Débito":
                            debito = debito + salePaymentMethod.getAmount();
                            break;
                        case "Efectivo":
                            efectivo = efectivo + salePaymentMethod.getAmount() - salePaymentMethod.getExchange();
                            break;
                        case "Cheques":
                            cheques = cheques + salePaymentMethod.getAmount();
                            break;
                        case "Transferencia bancaria":
                            transfer = transfer + salePaymentMethod.getAmount();
                            break;
                    }
                }
            }
        };
        SaleDayStat saleDayStat = new SaleDayStat();
        saleDayStat.setCredito(credito);
        saleDayStat.setDebito(debito);
        saleDayStat.setTransferencia(transfer);
        saleDayStat.setEfectivo(efectivo);
        saleDayStat.setTotal(credito + debito + transfer + efectivo);
        return saleDayStat;
    }

    //GET MOST SOLD PRODUCTS PER DAY IN A INTERVAL OF TIME
    public List<ProductSoldPerDayDto> getMostSoldProducts(Date firstDate, Date secondDate) {
        List<LocalDateTime> localDateTimeList = DateHelper.intervalDateToList(firstDate, secondDate, 0);
        List<ProductSoldPerDayDto> productSoldPerDayDtoList = new ArrayList<>();
        for (LocalDateTime localDateTime: localDateTimeList
             ) {
            ProductSoldPerDayDto productSoldPerDayDto = getMostSoldProductsPerDay(localDateTime);
            productSoldPerDayDtoList.add(productSoldPerDayDto);
        }
        return productSoldPerDayDtoList;
    }
    //GET LIST OF PRODUCT SOLD PER DAY
    private  ProductSoldPerDayDto getMostSoldProductsPerDay(LocalDateTime date){
        LocalDateTime datePlus1Day = date.plusDays(1);
        List<Sale> saleList = saleDao.findSaleByDateBetween(date, datePlus1Day);
        List<ProductSoldDto> productSoldDtoList = getTotalSoldProductsBySaleList(saleList);
        return new ProductSoldPerDayDto(date, productSoldDtoList);
    }
    //GET THE TOTAL SOLD PRODUCT WITHOUT REPEAT IT
    private List<ProductSoldDto> getTotalSoldProductsBySaleList(List<Sale> saleList){
        List<ProductSoldDto> productSoldDtoList = new ArrayList<>();
        for (Sale sale: saleList
             ) {
            for(SaleDetail saleDetail: sale.getSaleDetailList()){
                Product product = saleDetail.getProduct();
                ProductSoldDto productSoldDto = findProductSelledByIdIsIn(productSoldDtoList, product.getId());
                if(productSoldDto == null) {
                    productSoldDto = new ProductSoldDto();
                    productSoldDto.setCode(product.getCode());
                    productSoldDto.setCost(product.getCost());
                    productSoldDto.setId(product.getId());
                    productSoldDto.setSoldPrice(saleDetail.getUnitaryPrice());
                    productSoldDto.setTotalSoldQuantity(saleDetail.getQuantity());
                    productSoldDto.setProductName(saleDetail.getProductName());
                    productSoldDtoList.add(productSoldDto);
                }
                else{
                    productSoldDto.setTotalSoldQuantity(productSoldDto.getTotalSoldQuantity() + saleDetail.getQuantity());
                }
            }
        }
        productSoldDtoList.sort(Comparator.comparing(ProductSoldDto::getTotalSoldQuantity).reversed());

        return productSoldDtoList;
    }
    //OBTAIN THE PRODUCT IF THIS EXIST IN THE COLLECTION
    public ProductSoldDto findProductSelledByIdIsIn(Collection<ProductSoldDto> productSoldDtos, Integer id) {
        /*if(comodinIdList.contains(id)){
            return null;
        }*/
        return productSoldDtos.stream().filter(productSoldDto ->
                id.equals(productSoldDto.getId())).findFirst().orElse(null);
    }

    public List<ProductSoldDto>  getMostSoldProductsInInterval(Date firstDate, Date secondDate) {
        List<LocalDateTime> localDateTimeList = DateHelper.intervalDateToList(firstDate, secondDate, 1);
        List<Sale> saleList = saleDao.findSaleByDateBetween(localDateTimeList.get(0),
                localDateTimeList.get(localDateTimeList.size() - 1));
        List<ProductSoldDto> productSoldDtoList = getTotalSoldProductsBySaleList(saleList);
        productSoldDtoList.sort(Comparator.comparing(ProductSoldDto::getTotalSoldQuantity).reversed());
        return productSoldDtoList;

    }
}
