package ventas.backend.SQL.services;

import ventas.backend.SQL.dao.PaymentMethodDao;
import ventas.backend.SQL.dto.PaymentMethodDto;
import ventas.backend.SQL.mappers.PaymentMethodMapper;
import ventas.backend.SQL.models.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentMethodService {
    @Autowired
    private PaymentMethodDao paymentMethodDao;

    @Autowired
    private PaymentMethodMapper paymentMethodMapper;

    public List<PaymentMethodDto> getAllPaymentMethods(){
        List<PaymentMethod> paymentMethodList = paymentMethodDao.findAll();
        return paymentMethodMapper.mapToDtoArrayList(paymentMethodList);
    }

    public PaymentMethodDto getPaymentMethodById(Integer id){
        if(paymentMethodDao.findById(id).isPresent()){
            return paymentMethodMapper.mapToDto(paymentMethodDao.findPaymentMethodById(id));
        }
        else{
            return  null;
        }
    }
    public void updatePaymentMethod(PaymentMethodDto paymentMethodDto, Integer id){
        if(paymentMethodDao.findById(id).isPresent()){
            PaymentMethod paymentMethodFinded = paymentMethodDao.findPaymentMethodById(id);
            paymentMethodFinded.setType(paymentMethodDto.getType());
            paymentMethodFinded.setSalePaymentMethodList(paymentMethodDto.getSalePaymentMethodList());
            paymentMethodDao.save(paymentMethodFinded);

        }

    }
    public void deletePaymentMethod(Integer id){
        PaymentMethod paymentMethod = paymentMethodDao.findPaymentMethodById(id);
        paymentMethodDao.delete(paymentMethod);
    }

    public PaymentMethodDto createPaymentMethod(PaymentMethodDto paymentMethodDto){
        return paymentMethodMapper.mapToDto(paymentMethodDao.save(paymentMethodMapper.mapToModel(paymentMethodDto)));
    }
}