package tingeso.backend.SQL.dao;

import tingeso.backend.SQL.models.Product;
import tingeso.backend.SQL.models.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductDao extends JpaRepository<Product, Integer> {
    Product findProductById(Integer id);
    Product findProductByCode(String code);

    List<Product> findProductsBySubCategoryAndDeleted(SubCategory subCategory, Boolean deleted);

    /*@Query("select p from Product p, Category c, SubCategory s where c = s.catogory and s = p.subCategory and c = ?1")
    List<Product> findProductWithCategory(Category category);*/

    Boolean findExistByCode(String code);
    List<Product> findProductsByDeletedFalse();

    @Query("select p from Product p where p.published = true and p.deleted = false")
    List<Product> findWebProducts();

    @Query("select count(p) from Product p")
    Integer getProductQuantity();

    @Query(value = "SELECT product.* FROM product, sub_category, category " +
            "WHERE product.sub_category_id = sub_category.id AND sub_category.category_id = category.id " +
            "AND to_tsvector('spanish', product.code || ' ' || unaccent(product.module_name) || ' ' || unaccent(category.name) || ' ' || unaccent(sub_category.name)) @@ plainto_tsquery('spanish', :keyWord) " +
            "AND product.deleted = FALSE ", nativeQuery = true)
    List<Product> searchProductsAdminPage(@Param("keyWord") String keyWord);

    @Query(value = "select product.* From product WHERE (COALESCE(stock, 0) + COALESCE(warehouse_stock, 0) + COALESCE(event_stock, 0)) <= COALESCE( min_stock,0)", nativeQuery = true)
    List<Product> getProductWithLowStock();

}