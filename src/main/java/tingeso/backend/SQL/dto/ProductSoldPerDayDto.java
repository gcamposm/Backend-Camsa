package tingeso.backend.SQL.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductSoldPerDayDto {
    private LocalDateTime date;
    private List<ProductSoldDto> productSoldDtoList;

    public ProductSoldPerDayDto(LocalDateTime date, List<ProductSoldDto> productSoldDtoList) {
        this.date = date;
        this.productSoldDtoList = productSoldDtoList;
    }
}
