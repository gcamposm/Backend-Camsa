package tingeso.backend.SQL.mappers;

import tingeso.backend.SQL.dto.OneProductDto;
import tingeso.backend.SQL.models.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
@Component
public class OneProductMapper {

    public Product mapToModel(OneProductDto oneProductDto){
        Product product = new Product();
        product.setId(oneProductDto.getId());
        product.setSubCategory(oneProductDto.getSubCategory());
        product.setCode(oneProductDto.getCode());
        product.setCost(oneProductDto.getCost());
        product.setPriceLocal(oneProductDto.getPriceLocal());
        product.setPriceWeb(oneProductDto.getPriceWeb());
        product.setDescription(oneProductDto.getDescription());
        product.setProvider(oneProductDto.getProvider());
        product.setStock(oneProductDto.getStock());
        product.setModuleName(oneProductDto.getModuleName());
        product.setWarehouseStock(oneProductDto.getWarehouseStock());
        product.setLocalDiscount(oneProductDto.getLocalDiscount());
        product.setWebDiscount(oneProductDto.getWebDiscount());
        product.setWebName(oneProductDto.getWebName());
        product.setImageList(oneProductDto.getImageList());
        product.setEventStock(oneProductDto.getEventStock());
        return product;
    }

    public List<OneProductDto> mapToDtoArrayList(List<Product> products) {
        int i;

        ArrayList<OneProductDto> oneProductDtoArrayList = new ArrayList<>();
        for(i=0;i<products.size();i++){
            oneProductDtoArrayList.add(mapToDto(products.get(i)));
        }

        return oneProductDtoArrayList;
    }

    public OneProductDto mapToDto (Product product){

        OneProductDto oneProductDto = new OneProductDto();
        oneProductDto.setId(product.getId());
        oneProductDto.setSubCategory(product.getSubCategory());
        oneProductDto.setCode(product.getCode());
        oneProductDto.setCost(product.getCost());
        oneProductDto.setPriceLocal(product.getPriceLocal());
        oneProductDto.setPriceWeb(product.getPriceWeb());
        oneProductDto.setProvider(product.getProvider());
        oneProductDto.setDescription(product.getDescription());
        oneProductDto.setStock(product.getStock());
        oneProductDto.setModuleName(product.getModuleName());
        oneProductDto.setWarehouseStock(product.getWarehouseStock());
        oneProductDto.setLocalDiscount(product.getLocalDiscount());
        oneProductDto.setWebDiscount(product.getWebDiscount());
        oneProductDto.setWebName(product.getWebName());
        oneProductDto.setImageList(product.getImageList());
        oneProductDto.setEventStock(product.getEventStock());
        return oneProductDto;
    }
}
