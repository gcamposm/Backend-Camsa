package tingeso.backend.SQL.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties("productList")
public class ProviderDto {
    private Integer id;
    private String name;
    private Boolean deleted;
}