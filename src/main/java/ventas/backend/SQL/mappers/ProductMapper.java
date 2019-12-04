package ventas.backend.SQL.mappers;

import ventas.backend.SQL.dto.ProductDto;
import ventas.backend.SQL.models.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("Duplicates")
@Component
public class ProductMapper {
    public Product mapToModel(ProductDto productDto){

        Product product = new Product();
        product.setId(productDto.getId());
        product.setSubCategory(productDto.getSubCategory());
        product.setCode(productDto.getCode());
        product.setCost(productDto.getCost());
        product.setPriceLocal(productDto.getPriceLocal());
        product.setPriceWeb(productDto.getPriceWeb());
        product.setDescription(productDto.getDescription());
        product.setProvider(productDto.getProvider());
        product.setStock(productDto.getStock());
        product.setModuleName(productDto.getModuleName());
        product.setWarehouseStock(productDto.getWarehouseStock());
        product.setLocalDiscount(productDto.getLocalDiscount());
        product.setWebDiscount(productDto.getWebDiscount());
        product.setWebName(productDto.getWebName());
        product.setImageList(productDto.getImageList());
        product.setPublished(productDto.getPublished());
        product.setDeleted(productDto.getDeleted());
        product.setSize(productDto.getSize());
        product.setEventStock(productDto.getEventStock());
        return product;
    }

    public List<ProductDto> mapToDtoArrayList(List<Product> products) {
        int i;

        ArrayList<ProductDto> productDtoArrayList = new ArrayList<>();
        for(i=0;i<products.size();i++){
            productDtoArrayList.add(mapToDto(products.get(i)));
        }

        return productDtoArrayList;
    }

    public ProductDto mapToDto (Product product){

        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setSubCategory(product.getSubCategory());
        productDto.setCode(product.getCode());
        productDto.setCost(product.getCost());
        productDto.setPriceLocal(product.getPriceLocal());
        productDto.setPriceWeb(product.getPriceWeb());
        productDto.setProvider(product.getProvider());
        productDto.setDescription(product.getDescription());
        productDto.setStock(product.getStock());
        productDto.setModuleName(product.getModuleName());
        productDto.setWarehouseStock(product.getWarehouseStock());
        productDto.setLocalDiscount(product.getLocalDiscount());
        productDto.setWebDiscount(product.getWebDiscount());
        productDto.setWebName(product.getWebName());
        productDto.setImageList(product.getImageList());
        productDto.setPublished(product.getPublished());
        productDto.setDeleted(product.getDeleted());
        productDto.setSize(product.getSize());
        productDto.setEventStock(product.getEventStock());
        return productDto;
    }
}
