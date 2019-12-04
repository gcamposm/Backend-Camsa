package ventas.backend.SQL.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ventas.backend.SQL.models.Report;

public interface ReportDao extends JpaRepository<Report, Integer> {
    Report findReportById(Integer id);
}