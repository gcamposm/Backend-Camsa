package tingeso.backend.SQL.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleInformationDto {
    private int userId;
    private int deskId;
    private Integer saleId;
    private String description;
    private Boolean finalized;
    private BigDecimal positiveBalance;
    private ReducedSaleDetailDto[] reducedSaleDetailDtos;
}
