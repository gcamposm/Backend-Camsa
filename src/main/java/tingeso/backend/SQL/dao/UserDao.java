package tingeso.backend.SQL.dao;

import tingeso.backend.SQL.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface UserDao extends JpaRepository<User, Long> {
  ArrayList<User> findAll();
  ArrayList<User> findByFirstNameAndLastName(String firsName, String lastName);
  Optional<User> findByUsername(String username);
  //ArrayList<User> findByFirstNameAndLastName(String firsName, String lastName);
  User findById(long id);
  User findUserByUsername(String userName);
}
