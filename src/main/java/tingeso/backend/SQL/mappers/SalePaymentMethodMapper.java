package tingeso.backend.SQL.mappers;

import tingeso.backend.SQL.dto.SalePaymentMethodDto;
import tingeso.backend.SQL.models.SalePaymentMethod;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
@Component
public class SalePaymentMethodMapper {
    public SalePaymentMethod mapToModel(SalePaymentMethodDto salePaymentMethodDto){
        SalePaymentMethod salePaymentMethod = new SalePaymentMethod();
        salePaymentMethod.setId(salePaymentMethodDto.getId());
        salePaymentMethod.setAmount(salePaymentMethodDto.getAmount());
        salePaymentMethod.setExchange(salePaymentMethodDto.getExchange());
        salePaymentMethod.setSale(salePaymentMethodDto.getSale());
        salePaymentMethod.setPaymentMethod(salePaymentMethodDto.getPaymentMethod());
        return salePaymentMethod;
    }

    public SalePaymentMethodDto mapToDto(SalePaymentMethod salePaymentMethod){
        SalePaymentMethodDto salePaymentMethodDto = new SalePaymentMethodDto();
        salePaymentMethodDto.setId(salePaymentMethod.getId());
        salePaymentMethodDto.setAmount(salePaymentMethod.getAmount());
        salePaymentMethodDto.setExchange(salePaymentMethod.getExchange());
        salePaymentMethodDto.setSale(salePaymentMethod.getSale());
        salePaymentMethodDto.setPaymentMethod(salePaymentMethod.getPaymentMethod());
        return salePaymentMethodDto;
    }

    public List<SalePaymentMethodDto> mapToDtoList(List<SalePaymentMethod> salePaymentMethodList){
        List<SalePaymentMethodDto> salePaymentMethodDtoList = new ArrayList<>();
        for (SalePaymentMethod salePaymentMethod: salePaymentMethodList
        ) {
            SalePaymentMethodDto salePaymentMethodDto = new SalePaymentMethodDto();
            salePaymentMethodDto = mapToDto(salePaymentMethod);
            salePaymentMethodDtoList.add(salePaymentMethodDto);
        }
        return salePaymentMethodDtoList;
    }
}