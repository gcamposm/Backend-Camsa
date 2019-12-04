package tingeso.backend.SQL.dao;

import tingeso.backend.SQL.models.Image;
import tingeso.backend.SQL.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface ImageDao extends JpaRepository<Image, Integer> {
    Image findImageById(Integer id);

    Image findImageByName(String name);

    Image findTopByOrderByIdDesc();

    @Query("select i.id from Image i where i.principal = true and i.product =?1")
    Integer findImageByPrincipalEquals(Product product);

    Image findImageByProductAndPrincipal(Product product, Boolean principal);

    @Transactional
    @Modifying
    @Query("delete from Image i where i.id = ?1")
    void deleteImage(Integer saleDetailId);
}