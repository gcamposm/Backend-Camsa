package tingeso.backend.SQL.mappers;

import tingeso.backend.SQL.dto.ProductStockDto;
import tingeso.backend.SQL.models.Product;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductStockMapper {
    public ProductStockDto mapToDto(Product product){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(product, ProductStockDto.class);
    }

    public List<ProductStockDto> mapToDtoList(List<Product> productList){
        List<ProductStockDto> productStockDtoList = new ArrayList<>();
        for (Product product: productList
             ) {
            productStockDtoList.add(mapToDto(product));
        }
        return productStockDtoList;
    }
}
