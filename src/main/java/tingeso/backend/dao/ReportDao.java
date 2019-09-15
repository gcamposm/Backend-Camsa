package tingeso.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import tingeso.backend.model.Report;

public interface ReportDao extends JpaRepository<Report, Integer> {
    Report findReportById(Integer id);
}