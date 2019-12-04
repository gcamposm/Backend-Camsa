package tingeso.backend.SQL.dto;

import tingeso.backend.SQL.models.Entry;
import tingeso.backend.SQL.models.Sale;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties({"saleList", "entryList", "password"})
public class UserDto {
  private Integer id;
  private String firstName;
  private String lastName;
  private String userName;
  private String password;
  private Date birthDate;
  private Integer points;
  private Boolean active;
  private List<Sale> saleList;
  private List<Entry> entryList;
}