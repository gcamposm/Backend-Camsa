package ventas.backend.Mongo.services;


import ventas.backend.Mongo.mappers.CartSaleDetailMapper;
import ventas.backend.Mongo.models.Cart.Cart;
import ventas.backend.SQL.dao.ProductDao;
import ventas.backend.SQL.dao.SaleDao;
import ventas.backend.SQL.dto.SaleDto;
import ventas.backend.SQL.mappers.SaleMapper;
import ventas.backend.SQL.models.Product;
import ventas.backend.Mongo.dao.CartDao;
import ventas.backend.SQL.models.Sale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private CartDao cartDao;

    @Autowired
    private SaleDao saleDao;

    @Autowired
    private SaleMapper saleMapper;

    @Autowired
    private CartSaleDetailMapper cartSaleDetailMapper;

    public Cart addProduct(String cartId, Integer productId, Integer quantity){
        Product product = productDao.findProductById(productId);
        Cart currentCart = cartDao.findCartById(cartId);
        currentCart.addProduct(product, quantity);
        cartDao.save(currentCart);
        return currentCart;
    }

    public Cart removeProduct(String cartId, Integer productId){
        Product product = productDao.findProductById(productId);
        Cart currentCart = cartDao.findCartById(cartId);
        currentCart.removeProduct(product);
        cartDao.save(currentCart);
        return currentCart;
    }

    public Cart createCart(){
        Cart cart = new Cart();
        return cartDao.save(cart);
    }

    public Cart getCart(String cartId){
        return cartDao.findCartById(cartId);
    }

    public List<Cart> findAll(){
        return cartDao.findAll();
    }

    public SaleDto createSale(String id) {
        Cart cart = cartDao.findCartById(id);
        if(cart != null)
        {
            Sale sale = new Sale();
            sale.setSaleDetailList(cartSaleDetailMapper.mapToSQLSaleDetail(cart.getSaleDetailCartList()));
            sale.setTotal(cart.getTotal());
            sale.setTotalDiscount(cart.getDiscount());
            sale.setTotalWithDiscount(cart.getTotalWithDiscount());
            return saleMapper.mapToDto(saleDao.save(sale));
        }
        return null;
    }
}