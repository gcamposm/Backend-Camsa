package tingeso.backend.SQL.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Entity
@Table(name="users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id;

  @NotEmpty
  private String username;
  @JsonIgnore
  @NotEmpty
  private String password;
  private String firstName;
  private String lastName;
  private Boolean active;
  private Integer points;
  @Column(name = "birth_date") @Temporal(TemporalType.DATE)
  private Date birthDate;
  // RELATIONS
  // role -> user
  @ElementCollection(fetch = FetchType.EAGER)
  @Builder.Default
  private List<String> roles = new ArrayList<>();

  @OneToMany(mappedBy = "userventas", cascade = CascadeType.ALL)
  @JsonIgnore
  private List<Sale> saleList;

  //user -> entry
  @OneToMany(mappedBy = "userventas", cascade = CascadeType.ALL)
  @JsonIgnore
  private List<Entry> entryList;

  //user -> desk
  @OneToMany(mappedBy = "userventas", cascade = CascadeType.ALL)
  @JsonIgnore
  private List<Desk> deskList;


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.roles.stream().map(SimpleGrantedAuthority::new).collect(toList());
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
