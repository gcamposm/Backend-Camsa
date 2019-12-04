package ventas.backend.SQL.mappers;

import ventas.backend.SQL.dto.ReducedSaleDetailDto;
import ventas.backend.SQL.dto.SaleDetailDto;
import ventas.backend.SQL.models.SaleDetail;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
@Component
public class SaleDetailMapper {
    public SaleDetail mapToModel(SaleDetailDto saleDetailDto){
        SaleDetail saleDetail = new SaleDetail();
        saleDetail.setId(saleDetailDto.getId());
        saleDetail.setQuantity(saleDetailDto.getQuantity());
        saleDetail.setDiscount(saleDetailDto.getDiscount());
        saleDetail.setProduct(saleDetailDto.getProduct());
        saleDetail.setSale(saleDetailDto.getSale());
        saleDetail.setSubtotal(saleDetailDto.getSubtotal());
        saleDetail.setTotalWithDiscount(saleDetailDto.getTotalWithDiscount());
        saleDetail.setUnitaryPrice(saleDetailDto.getUnitaryPrice());

        return saleDetail;
    }
    public SaleDetailDto mapToDto(SaleDetail saleDetail){
        SaleDetailDto saleDetailDto = new SaleDetailDto();
        saleDetailDto.setId(saleDetail.getId());
        saleDetailDto.setQuantity(saleDetail.getQuantity());
        saleDetailDto.setDiscount(saleDetail.getDiscount());
        saleDetailDto.setProduct(saleDetail.getProduct());
        saleDetailDto.setSale(saleDetail.getSale());
        saleDetailDto.setSubtotal(saleDetail.getSubtotal());
        saleDetailDto.setTotalWithDiscount(saleDetail.getTotalWithDiscount());
        saleDetailDto.setUnitaryPrice(saleDetail.getUnitaryPrice());
        return saleDetailDto;
    }
    public List<SaleDetailDto> mapToDtoList(List<SaleDetail> saleList){
        List<SaleDetailDto> saleDetailDtoList = new ArrayList<>();
        for (SaleDetail saleDetail: saleList
        ) {
            SaleDetailDto saleDetailDto = new SaleDetailDto();
            saleDetailDto = mapToDto(saleDetail);
            saleDetailDtoList.add(saleDetailDto);
        }
        return saleDetailDtoList;
    }
    public ReducedSaleDetailDto mapToReducedSaleDetailDto(SaleDetail saleDetail){
        ReducedSaleDetailDto reducedSaleDetailDto = new ReducedSaleDetailDto();
        reducedSaleDetailDto.setSubTotal(saleDetail.getSubtotal());
        reducedSaleDetailDto.setQuantity(saleDetail.getQuantity());
        reducedSaleDetailDto.setProductId(saleDetail.getProduct().getId());
        reducedSaleDetailDto.setDiscount(saleDetail.getDiscount());
        reducedSaleDetailDto.setName(saleDetail.getProductName());
        reducedSaleDetailDto.setSubTotalWithDiscount(saleDetail.getTotalWithDiscount());
        reducedSaleDetailDto.setUnitaryPrice(saleDetail.getUnitaryPrice());
        return reducedSaleDetailDto;
    }

    public List<ReducedSaleDetailDto> mapToReducedSaleDetailDtoList(List<SaleDetail> saleDetailList){
        List<ReducedSaleDetailDto> reducedSaleDetailDtoList = new ArrayList<>();
        for (SaleDetail saleDetail: saleDetailList
        ) {
            ReducedSaleDetailDto reducedSaleDetailDto = new ReducedSaleDetailDto();
            reducedSaleDetailDto = mapToReducedSaleDetailDto(saleDetail);
            reducedSaleDetailDtoList.add(reducedSaleDetailDto);
        }
        return reducedSaleDetailDtoList;
    }
}
