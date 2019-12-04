package ventas.backend.SQL.dao;

import ventas.backend.SQL.models.Sale;
import ventas.backend.SQL.models.SalePaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SalePaymentMethodDao extends JpaRepository<SalePaymentMethod, Integer> {
    SalePaymentMethod findSalePaymentMethodById(Integer id);

    @Query("select sum(s.amount) from SalePaymentMethod s where s.sale = ?1")
    Integer getActualyPaymentAmountForSale(Sale sale);

    List<SalePaymentMethod> getSalePaymentMethodBySale(Sale sale);
}