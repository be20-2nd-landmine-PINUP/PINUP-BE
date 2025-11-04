package pinup.backend.report.query.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pinup.backend.report.query.domain.Report;
import pinup.backend.report.query.dto.ReportListResponse;
import pinup.backend.report.query.dto.ReportSpecificResponse;
import pinup.backend.report.query.repository.ReportRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;

    public List<ReportListResponse> getAllReport() {
        List<Report> reports = reportRepository.findAll();

        return reports.stream().map(report -> ReportListResponse.builder()
                .report_id(report.getReportId())
                .user_id(report.getUser().getId().intValue())
                .reason(report.getReason())
                .status(report.getStatus().toString())
                .created_at(report.getCreatedAt())
                .build()).toList();
    }

    public ReportSpecificResponse getSpecificReport(Integer reportId) {
        Report report = reportRepository.findByReportId(reportId);

        return ReportSpecificResponse.builder()
                .report_id(report.getReportId())
                .user_id(report.getUser().getId().intValue())
                .reason(report.getReason())
                .status(report.getStatus().toString())
                .created_at(report.getCreatedAt())
                .admin_id(report.getAdmin().getId())
                .updated_at(report.getUpdatedAt())
                .created_at(report.getCreatedAt())
                .build();
    }
}
