package tingeso.backend.SQL.mappers;

import tingeso.backend.SQL.dto.PaymentMethodDto;
import tingeso.backend.SQL.models.PaymentMethod;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
@Component
public class PaymentMethodMapper {
    public PaymentMethod mapToModel(PaymentMethodDto paymentMethodDto){

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(paymentMethodDto.getId());
        paymentMethod.setType(paymentMethodDto.getType());
        paymentMethod.setSalePaymentMethodList(paymentMethodDto.getSalePaymentMethodList());
        return paymentMethod;
    }

    public List<PaymentMethodDto> mapToDtoArrayList(List<PaymentMethod> paymentMethodList) {
        int i;

        List<PaymentMethodDto> paymentMethodDtoArrayList = new ArrayList<>();
        for(i=0;i<paymentMethodList.size();i++){
            paymentMethodDtoArrayList.add(mapToDto(paymentMethodList.get(i)));
        }

        return paymentMethodDtoArrayList;
    }

    public PaymentMethodDto mapToDto (PaymentMethod paymentMethod){

        PaymentMethodDto paymentMethodDto = new PaymentMethodDto();
        paymentMethodDto.setId(paymentMethod.getId());
        paymentMethodDto.setType(paymentMethod.getType());
        paymentMethodDto.setSalePaymentMethodList(paymentMethod.getSalePaymentMethodList());
        return paymentMethodDto;
    }
}