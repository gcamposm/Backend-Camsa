package ventas.backend.SQL.dao;

import ventas.backend.SQL.models.Desk;
import ventas.backend.SQL.models.PhysicalDesk;
import ventas.backend.SQL.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeskDao  extends JpaRepository<Desk, Integer> {
    Desk findDeskById(Integer id);
    //Desk findTopByOrderByIdDescPhysicalDeskAsc(PhysicalDesk physicalDesk);
    List<Desk> findDeskByPhysicalDeskOrderByIdDesc(PhysicalDesk physicalDesk);
    Desk findDeskByIsOpenAndPhysicalDeskOrderByIdDesc(Boolean isOpen, PhysicalDesk physicalDesk);
    Desk findTopByOrderByIdDesc();
    Desk findDeskByIsOpenAndUserventas(Boolean isOpen, User userventas);
    Desk findDeskByIsOpenAndPhysicalDesk(Boolean isOpen, PhysicalDesk physicalDesk);
    Desk findFirstByIsOpenAndPhysicalDeskOrderByIdDesc(Boolean isOpen, PhysicalDesk physicalDesk);
    Desk findDeskByIsOpenAndUserventasAndPhysicalDesk(Boolean isOpen, User userventas, PhysicalDesk physicalDesk);
}