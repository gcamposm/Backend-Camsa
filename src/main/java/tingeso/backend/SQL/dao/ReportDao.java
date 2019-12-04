package tingeso.backend.SQL.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import tingeso.backend.SQL.models.Report;

public interface ReportDao extends JpaRepository<Report, Integer> {
    Report findReportById(Integer id);
}