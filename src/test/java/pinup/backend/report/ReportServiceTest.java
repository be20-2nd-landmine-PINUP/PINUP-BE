package pinup.backend.report;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.report.query.dto.ReportListResponse;
import pinup.backend.report.query.dto.ReportSpecificResponse;
import pinup.backend.report.query.service.ReportService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/*
* db에 실제 데이터 있어야 테스트 검증 가능
 */
@SpringBootTest
@Transactional
public class ReportServiceTest {

    @Autowired
    private ReportService reportService;

    @Test
    @DisplayName("신고 목록이 조회된다")
    void getAllReportTest() {
        List<ReportListResponse> reports = reportService.getAllReport();

        assertThat(reports).isNotNull();
        assertThat(reports.size()).isGreaterThan(0);
        reports.forEach(report -> {
            assertThat(report.getReportId()).isNotNull();
            assertThat(report.getReason()).isNotNull();
            assertThat(report.getStatus()).isNotNull();
        });
    }

    @Test
    @DisplayName("신고 상세내역이 조회된다")
    void getSpecificReportTest() {
        Long reportId = 1L;
        ReportSpecificResponse report = reportService.getSpecificReport(reportId);

        assertThat(report).isNotNull();
        assertThat(report.getReportId()).isEqualTo(reportId);
        assertThat(report.getReason()).isNotNull();
        assertThat(report.getUserId()).isNotNull();
        assertThat(report.getAdminId()).isNotNull();
        assertThat(report.getStatus()).isNotNull();
        assertThat(report.getCreatedAt()).isNotNull();
        assertThat(report.getUpdatedAt()).isNotNull();
    }
}
