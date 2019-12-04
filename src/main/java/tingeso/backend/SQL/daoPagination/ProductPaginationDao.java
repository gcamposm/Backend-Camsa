package tingeso.backend.SQL.daoPagination;

import tingeso.backend.SQL.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductPaginationDao extends PagingAndSortingRepository<Product, Integer> {
    List<Product> findByOrderByModuleNameAsc(Pageable pageable);

    @Query(value = "SELECT product.* FROM product, sub_category, category " +
            "WHERE product.sub_category_id = sub_category.id" +
            " AND sub_category.category_id = category.id " + "AND product.published = TRUE " +
            "AND category.id = :id " +
            "AND product.deleted = FALSE " +
            "AND product.price_web BETWEEN :fromPrice AND :toPrice " +
            "INTERSECT Select product.* from product, image where image.product_id = product.id ", nativeQuery = true)
    Page<Product> selectByCategory(@Param("id") Integer id,
                                   @Param("fromPrice") int fromPrice,
                                   @Param("toPrice") int toPrice,
                                   Pageable pageable);

    @Query(value = "SELECT product.* FROM product, sub_category " +
            "WHERE product.sub_category_id = :id " +
            "AND product.price_web BETWEEN :fromPrice AND :toPrice " +
            "AND product.published = TRUE " +
            "AND product.deleted = FALSE " +
            "INTERSECT SELECT product.* FROM product, image WHERE image.product_id = product.id ", nativeQuery = true)
    Page<Product> selectBySubCategory(@Param("id") Integer id,
                                      @Param("fromPrice") int fromPrice,
                                      @Param("toPrice") int toPrice,
                                      Pageable pageable);

    @Query(value = "SELECT product.* FROM product, sub_category " +
            "WHERE product.price_web BETWEEN :fromPrice AND :toPrice " +
            "AND product.published = TRUE AND product.sub_category_id = sub_category.id " +
            "AND product.deleted = FALSE " +
            "INTERSECT SELECT product.* FROM product, image WHERE image.product_id = product.id ", nativeQuery = true)
    Page<Product> selectAllProductWithFilters(@Param("fromPrice") int fromPrice,
                                              @Param("toPrice") int toPrice,
                                              Pageable pageable);

    @Query(value = "SELECT product.* FROM product, sub_category, category " +
            "WHERE product.sub_category_id = sub_category.id AND sub_category.category_id = category.id " +
            "AND to_tsvector('spanish', unaccent(product.web_name) || ' ' || unaccent(category.name) || ' ' || unaccent(sub_category.name) || ' ' || unaccent(COALESCE( product.description,''))) @@ plainto_tsquery('spanish',:keyWord) " +
            "AND product.published = TRUE " +
            "AND product.deleted = FALSE " +
            "INTERSECT SELECT product.* FROM product, image WHERE image.product_id = product.id ", nativeQuery = true)
    Page<Product> searchProducts(@Param("keyWord") String keyWord, Pageable pageable);
}
