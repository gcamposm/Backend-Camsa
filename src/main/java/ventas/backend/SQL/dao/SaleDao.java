package ventas.backend.SQL.dao;

import ventas.backend.SQL.models.Desk;
import ventas.backend.SQL.models.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleDao extends JpaRepository<Sale, Integer> {
    Sale findSaleById(Integer id);
    Sale findTopByOrderByIdDesc();
    Sale findSaleByTicketNumber(Integer ticketNumber);
    List<Sale> findSalesByDesk(Desk desk);
    List<Sale> findSaleByDeskAndFinalized(Desk desk, Boolean finalized);
    List<Sale> findAllByFinalized(Boolean finalized);
    List<Sale> findSaleByDateBetween(LocalDateTime firstDate, LocalDateTime secondDate);
    List<Sale> findSaleByDate(LocalDateTime firstDate);
}
