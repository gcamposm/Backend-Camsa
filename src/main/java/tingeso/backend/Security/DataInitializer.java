package tingeso.backend.Security;

import tingeso.backend.SQL.dao.UserDao;
import tingeso.backend.SQL.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@Order(1)
public class DataInitializer implements CommandLineRunner {



    @Autowired
    UserDao users;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        List<User> userList = users.findAll();
        if(userList.size() > 0){
            return;
        }
        else {
            //users.deleteAll();
            this.users.save(User.builder()
                    .username("user")
                    .password(this.passwordEncoder.encode("password"))
                    .roles(Arrays.asList("ROLE_USER"))
                    .build()
            );

            this.users.save(User.builder()
                    .username("admin")
                    .password(this.passwordEncoder.encode("tasmania"))
                    .roles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"))
                    .build()
            );
        }

        //log.debug("printing all users...");
        //this.users.findAll().forEach(v -> log.debug(" User :" + v.toString()));
    }
}
