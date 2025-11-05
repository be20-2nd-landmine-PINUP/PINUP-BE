package pinup.backend.report.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ReportListResponse {
    public Long reportId;
    public Long userId;
    public String reason;
    public String status;
    public LocalDateTime createdAt;
}
