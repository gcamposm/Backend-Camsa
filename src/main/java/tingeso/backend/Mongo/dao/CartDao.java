package tingeso.backend.Mongo.dao;

import tingeso.backend.Mongo.models.Cart.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CartDao extends MongoRepository<Cart, String> {
    Cart findCartById(String id);
}
