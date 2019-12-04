package tingeso.backend.SQL.dao;

import tingeso.backend.SQL.models.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProviderDao extends JpaRepository<Provider, Integer> {
    Provider findProviderByName(String name);
    Provider findProviderById(Integer id);

    List<Provider> findProviderByDeletedFalse();
}