package tingeso.backend.SQL.dao;

import tingeso.backend.SQL.models.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryDao extends JpaRepository<Entry, Integer> {
    Entry findEntryById(Integer id);

}
