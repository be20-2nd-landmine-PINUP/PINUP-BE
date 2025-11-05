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
public class ReportSpecificResponse {
    public Long reportId;
    public Long userId;
    public Long feedId;
    public Long adminId;
    public String reason;
    public String status;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
}
