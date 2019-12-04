package ventas.backend.Mongo.mappers;

import ventas.backend.Mongo.models.Cart.SaleDetailCart;
import ventas.backend.SQL.dao.SaleDetailDao;
import ventas.backend.SQL.mappers.SaleDetailMapper;
import ventas.backend.SQL.models.SaleDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
@Component
public class CartSaleDetailMapper {

    @Autowired
    private CartProductMapper cartProductMapper;

    @Autowired
    private SaleDetailMapper saleDetailMapper;

    @Autowired
    private SaleDetailDao saleDetailDao;

    public List<SaleDetail> mapToSQLSaleDetail(List<SaleDetailCart> saleDetailCartList) {
        int i;
        List<SaleDetail> saleDetailDtoList = new ArrayList<>();
        for(i=0;i<saleDetailCartList.size();i++) {
            saleDetailDtoList.add(mapToSQL(saleDetailCartList.get(i)));
        }
        return saleDetailDtoList;
    }

    public SaleDetail mapToSQL(SaleDetailCart saleDetailCart){
        SaleDetail saleDetail = new SaleDetail();
        saleDetail.setDiscount(saleDetailCart.getDiscount());
        saleDetail.setProduct(cartProductMapper.mapToSQL(saleDetailCart.getProduct()));
        saleDetail.setSubtotal(saleDetailCart.getSubtotal());
        saleDetail.setTotalWithDiscount(saleDetailCart.getTotalWithDiscount());
        saleDetail.setQuantity(saleDetailCart.getQuantity());
        return saleDetailDao.save(saleDetail);
    }
}