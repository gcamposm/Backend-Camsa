package ventas.backend.SQL.dao;

import ventas.backend.SQL.models.Entry;
import ventas.backend.SQL.models.EntryDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface EntryDetailDao extends JpaRepository<EntryDetail, Integer> {
    EntryDetail findEntryDetailById(Integer id);
    List<EntryDetail> findEntryDetailsByEntry(Entry entry);

    @Query("select sum(e.subTotal) from EntryDetail e where e.entry = ?1")
    BigDecimal sumAllSubTotals(Entry entry);
}
