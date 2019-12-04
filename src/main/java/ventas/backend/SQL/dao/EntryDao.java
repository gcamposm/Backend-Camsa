package ventas.backend.SQL.dao;

import ventas.backend.SQL.models.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryDao extends JpaRepository<Entry, Integer> {
    Entry findEntryById(Integer id);

}
