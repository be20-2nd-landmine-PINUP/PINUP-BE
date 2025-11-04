package pinup.backend.report.query.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pinup.backend.report.query.dto.ReportListResponse;
import pinup.backend.report.query.dto.ReportSpecificResponse;
import pinup.backend.report.query.service.ReportService;

import java.util.List;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<List<ReportListResponse>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReport());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportSpecificResponse> getReportById(@PathVariable Integer id) {
        return ResponseEntity.ok(reportService.getSpecificReport(id));
    }
}
