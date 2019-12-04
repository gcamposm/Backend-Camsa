package ventas.backend.SQL.dao;

import ventas.backend.SQL.models.PhysicalDesk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhysicalDeskDao  extends JpaRepository<PhysicalDesk, Integer> {
    PhysicalDesk findPhysicalDeskById(Integer id);
}