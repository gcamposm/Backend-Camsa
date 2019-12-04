package tingeso.backend.SQL.services;

import tingeso.backend.SQL.dao.PaymentMethodDao;
import tingeso.backend.SQL.dao.SaleDao;
import tingeso.backend.SQL.dao.SalePaymentMethodDao;
import tingeso.backend.SQL.dto.DeskDto;
import tingeso.backend.SQL.dto.SalePaymentMethodDto;
import tingeso.backend.SQL.dto.SaleWithPaymentMethodEncapsulatedDto;
import tingeso.backend.SQL.dto.SaleWithPaymentMethodsDto;
import tingeso.backend.SQL.mappers.SalePaymentMethodMapper;
import tingeso.backend.SQL.models.PaymentMethod;
import tingeso.backend.SQL.models.Sale;
import tingeso.backend.SQL.models.SalePaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SalePaymentMethodService {
    @Autowired
    private SalePaymentMethodDao salePaymentMethodDao;

    @Autowired
    private PaymentMethodDao paymentMethodDao;

    @Autowired
    private SaleDao saleDao;

    @Autowired
    private SalePaymentMethodMapper salePaymentMethodMapper;

    @Autowired
    private DeskService deskService;

    public List<SalePaymentMethodDto> getAllSalePaymentMethod(){
        List<SalePaymentMethod> salePaymentMethodList = salePaymentMethodDao.findAll();
        return salePaymentMethodMapper.mapToDtoList(salePaymentMethodList);
    }

    public SalePaymentMethodDto getSalePaymentMethodById(Integer id){
        if(salePaymentMethodDao.findById(id).isPresent()){
            return salePaymentMethodMapper.mapToDto(salePaymentMethodDao.findSalePaymentMethodById(id));
        }
        else{
            return  null;
        }
    }
    public void updateSalePaymentMethod(SalePaymentMethodDto salePaymentMethodDto, Integer id){
        if(salePaymentMethodDao.findById(id).isPresent()){
            SalePaymentMethod salePaymentMethodFinded = salePaymentMethodDao.findSalePaymentMethodById(id);
            salePaymentMethodFinded.setAmount(salePaymentMethodDto.getAmount());
            salePaymentMethodFinded.setExchange(salePaymentMethodDto.getExchange());
            salePaymentMethodFinded.setPaymentMethod(salePaymentMethodDto.getPaymentMethod());
            salePaymentMethodFinded.setSale(salePaymentMethodDto.getSale());
            salePaymentMethodDao.save(salePaymentMethodFinded);

        }

    }

    public SalePaymentMethodDto createSalePaymentMethod(SalePaymentMethodDto salePaymentMethodDto){
        return salePaymentMethodMapper.mapToDto(salePaymentMethodDao.save(salePaymentMethodMapper.mapToModel(salePaymentMethodDto)));
    }

    public void deleteSalePaymentMethod(Integer id) {
        SalePaymentMethod salePaymentMethod = salePaymentMethodDao.findSalePaymentMethodById(id);
        salePaymentMethodDao.delete(salePaymentMethod);
    }

    public SalePaymentMethodDto createSalePaymentMethodWithSale(Integer saleId, Integer paymentMethodId, Integer amount, Integer exchange){
        PaymentMethod paymentMethod = paymentMethodDao.findPaymentMethodById(paymentMethodId);
        Sale sale = saleDao.findSaleById(saleId);
        List<SalePaymentMethod> salePaymentMethodList = sale.getSalePaymentMethodList();
        for (SalePaymentMethod salePaymentMethod: salePaymentMethodList
             ) {
            System.out.println("SalePMID: " + salePaymentMethod.getId() + " PMID: " + paymentMethodId);
            if(salePaymentMethod.getPaymentMethod().getId().equals(paymentMethodId)){
                System.out.println("----------------> existe");
                salePaymentMethod.setAmount(amount);
                salePaymentMethod.setExchange(exchange);
                salePaymentMethod.setPaymentMethod(paymentMethod);
                salePaymentMethod.setSale(sale);
                return salePaymentMethodMapper.mapToDto(salePaymentMethodDao.save(salePaymentMethod));
            }
        }
        SalePaymentMethod salePaymentMethod = new SalePaymentMethod();
        salePaymentMethod.setAmount(amount);
        salePaymentMethod.setExchange(exchange);
        salePaymentMethod.setPaymentMethod(paymentMethod);
        salePaymentMethod.setSale(sale);
        return salePaymentMethodMapper.mapToDto(salePaymentMethodDao.save(salePaymentMethod));
    }
    public BigDecimal getActualyPaymentAmountForSale(Sale sale){
        Integer actualTotalPayment = salePaymentMethodDao.getActualyPaymentAmountForSale(sale);
        return new BigDecimal(actualTotalPayment);
    }

    public List<SalePaymentMethodDto> getSalePaymentMethodForSale(int id) {
        Sale sale = saleDao.findSaleById(id);
        List<SalePaymentMethod> salePaymentMethodList =  salePaymentMethodDao.getSalePaymentMethodBySale(sale);
        return salePaymentMethodMapper.mapToDtoList(salePaymentMethodList);
    }

    public List<SaleWithPaymentMethodEncapsulatedDto> getPaymentMethodForAllSalesInADesk(int deskId) {
        List<SaleWithPaymentMethodEncapsulatedDto> saleWithPaymentMethodEncapsulatedDtoList = new ArrayList<>();


        DeskDto desk = deskService.getDeskById(deskId);
        if(desk != null){
            List<Sale> saleList =  desk.getSaleList();
            for (Sale sale: saleList){
                List<SaleWithPaymentMethodsDto> saleWithPaymentMethodsDtoList = new ArrayList<>();
                List<SalePaymentMethod> salePaymentMethodList =  sale.getSalePaymentMethodList();
                for (SalePaymentMethod salePaymentMethod: salePaymentMethodList){
                    if(salePaymentMethod.getAmount() != 0) {
                        SaleWithPaymentMethodsDto saleWithPaymentMethodsDto = new SaleWithPaymentMethodsDto(
                                sale.getTicketNumber(),
                                sale.getTotalWithDiscount(),
                                salePaymentMethod.getAmount(),
                                salePaymentMethod.getPaymentMethod().getType()
                        );
                        saleWithPaymentMethodsDtoList.add(saleWithPaymentMethodsDto);
                    }
                }
                //Encapsulating the sale with payment method por detail statistics
                SaleWithPaymentMethodEncapsulatedDto saleWithPaymentMethodEncapsulatedDto =
                        new SaleWithPaymentMethodEncapsulatedDto(
                        sale.getTicketNumber(),
                        sale.getTotalWithDiscount(),
                        saleWithPaymentMethodsDtoList
                );
                saleWithPaymentMethodEncapsulatedDtoList.add(saleWithPaymentMethodEncapsulatedDto);
            }
        }
        return saleWithPaymentMethodEncapsulatedDtoList;
    }
}