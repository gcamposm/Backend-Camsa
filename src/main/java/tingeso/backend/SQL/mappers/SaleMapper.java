package tingeso.backend.SQL.mappers;

import tingeso.backend.SQL.dto.SaleDto;
import tingeso.backend.SQL.models.Sale;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
@Component
public class SaleMapper {
    public Sale mapToModel(SaleDto saleDto){
        Sale sale = new Sale();
        sale.setId(saleDto.getId());
        sale.setDate(saleDto.getDate());
        sale.setTotal(saleDto.getTotal());
        sale.setSaleDetailList(saleDto.getSaleDetailList());
        sale.setVoucherType(saleDto.getVoucherType());
        sale.setFormatDate(saleDto.getFormatDate());
        sale.setTicketNumber(saleDto.getTicketNumber());
        sale.setFinalized(saleDto.getFinalized());
        sale.setDescription(saleDto.getDescription());
        sale.setTotalQuantity(saleDto.getTotalQuantity());
        sale.setTotalWithDiscount(saleDto.getTotalWithDiscount());
        sale.setTotalDiscount(saleDto.getTotalDiscount());
        sale.setDesk(saleDto.getDesk());
        sale.setPositiveBalance(saleDto.getPositiveBalance());
        return sale;
    }
    public SaleDto mapToDto(Sale sale){
        SaleDto saleDto = new SaleDto();
        saleDto.setFormatDate(sale.getFormatDate());
        saleDto.setId(sale.getId());
        saleDto.setDate(sale.getDate());
        saleDto.setSaleDetailList(sale.getSaleDetailList());
        saleDto.setTotal(sale.getTotal());
        saleDto.setUserName(sale.getUserventas().getUsername());
        saleDto.setVoucherType(sale.getVoucherType());
        saleDto.setTicketNumber(sale.getTicketNumber());
        saleDto.setFinalized(sale.getFinalized());
        saleDto.setDescription(sale.getDescription());
        saleDto.setTotalQuantity(sale.getTotalQuantity());
        saleDto.setTotalWithDiscount(sale.getTotalWithDiscount());
        saleDto.setTotalDiscount(sale.getTotalDiscount());
        saleDto.setDesk(sale.getDesk());
        saleDto.setPositiveBalance(sale.getPositiveBalance());
        return saleDto;
    }

    public List<SaleDto> mapToDtoList(List<Sale> saleList){

        List<SaleDto> saleDtoList = new ArrayList<>();
        for (Sale sale: saleList
        ) {
            SaleDto saleDto;
            saleDto = mapToDto(sale);
            saleDtoList.add(saleDto);
        }
        return saleDtoList;
    }

}
