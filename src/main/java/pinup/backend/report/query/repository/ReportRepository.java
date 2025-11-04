package pinup.backend.report.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pinup.backend.report.query.domain.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report,String> {
    Report findByReportId(Integer id);
}
