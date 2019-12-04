package tingeso.backend.SQL.dao;

import tingeso.backend.SQL.models.PhysicalDesk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhysicalDeskDao  extends JpaRepository<PhysicalDesk, Integer> {
    PhysicalDesk findPhysicalDeskById(Integer id);
}