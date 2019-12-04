package tingeso.backend.Mongo.mappers;

import tingeso.backend.Mongo.models.Cart.CartProduct;
import tingeso.backend.SQL.dto.ProductDto;
import tingeso.backend.SQL.models.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
@Component
public class CartProductMapper {

    public List<ProductDto> mapToSQLProduct(List<CartProduct> cartProductList) {
        int i;
        List<ProductDto> productDtoArrayList = new ArrayList<>();
        for(i=0;i<cartProductList.size();i++) {
            productDtoArrayList.add(mapToDtoSQL(cartProductList.get(i)));
        }
        return productDtoArrayList;
    }

    public ProductDto mapToDtoSQL(CartProduct cartProduct){
        ProductDto productDto = new ProductDto();
        productDto.setId(cartProduct.getId());
        productDto.setWebDiscount(cartProduct.getDiscount());
        productDto.setCode(cartProduct.getCode());
        productDto.setPriceWeb(cartProduct.getPrice());
        productDto.setWebName(cartProduct.getName());
        return productDto;
    }

    public Product mapToSQL(CartProduct cartProduct){
        Product product = new Product();
        product.setId(cartProduct.getId());
        product.setWebDiscount(cartProduct.getDiscount());
        product.setCode(cartProduct.getCode());
        product.setPriceWeb(cartProduct.getPrice());
        product.setWebName(cartProduct.getName());
        return product;
    }
}
