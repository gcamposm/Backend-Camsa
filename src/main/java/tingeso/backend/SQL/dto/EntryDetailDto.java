package tingeso.backend.SQL.dto;

import tingeso.backend.SQL.models.Entry;
import tingeso.backend.SQL.models.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class EntryDetailDto {
    private Integer id;
    private Integer quantity;
    private String type;
    private Boolean sumStock;
    private Product product;
    @JsonIgnore
    private Entry entry;
}
