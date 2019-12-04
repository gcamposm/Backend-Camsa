package tingeso.backend.SQL.dao;

import tingeso.backend.SQL.models.Sale;
import tingeso.backend.SQL.models.SaleDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;


public interface SaleDetailDao extends JpaRepository<SaleDetail, Integer> {
    SaleDetail findSaleDetailById(Integer id);

    @Query("select sum(s.subtotal) from SaleDetail s where s.sale = ?1")
    BigDecimal sumAllProductsBySaleId(Sale sale);

    @Query("select sum(s.quantity) from SaleDetail s where s.sale = ?1")
    Integer sumAllQuantity(Sale sale);

    @Query("select sum(s.discount) from SaleDetail s where s.sale = ?1")
    BigDecimal sumAllDiscount(Sale sale);

    @Query("select sum(s.totalWithDiscount) from SaleDetail s where s.sale = ?1")
    BigDecimal sumAllTotalsWithDiscounts(Sale sale);

    @Transactional
    @Modifying
    @Query("delete from SaleDetail e where e.id = ?1")
    void deleteSaleDetail(Integer saleDetailId);

    List<SaleDetail> findSaleDetailBySale(Sale sale);
}