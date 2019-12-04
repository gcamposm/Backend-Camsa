package tingeso.backend.SQL.mappers;

import tingeso.backend.SQL.dto.UserDto;
import tingeso.backend.SQL.models.User;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
@Component
public class UserMapper {

  public User mapToModel(UserDto userDto){
    User user = new User();
    user.setId(userDto.getId().longValue());
    user.setBirthDate(userDto.getBirthDate());
    user.setFirstName(userDto.getFirstName());
    user.setLastName(userDto.getLastName());
    user.setUsername(userDto.getUserName());
    user.setPassword(userDto.getPassword());
    user.setSaleList(userDto.getSaleList());
    user.setEntryList(userDto.getEntryList());
    user.setActive(userDto.getActive());
    user.setPoints(userDto.getPoints());
    return user;
  }

  public List<UserDto> mapToDtoList(List<User> user) {
    int i;

    ArrayList<UserDto> usersDto = new ArrayList<>();
    for(i=0;i<user.size();i++){
      usersDto.add(mapToDto(user.get(i)));
    }

    return usersDto;
  }

  public UserDto mapToDto (User user){

    UserDto userDto = new UserDto();
    userDto.setId(user.getId().intValue());
    userDto.setBirthDate(user.getBirthDate());
    userDto.setFirstName(user.getFirstName());
    userDto.setLastName(user.getLastName());
    userDto.setUserName(user.getUsername());
    userDto.setPassword(user.getPassword());
    userDto.setSaleList(user.getSaleList());
    userDto.setEntryList(user.getEntryList());
    userDto.setActive(user.getActive());
    userDto.setPoints(user.getPoints());
    return userDto;
  }
}