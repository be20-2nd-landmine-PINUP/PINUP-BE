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
    public Integer reportId;
    public Integer userId;
    public Integer feedId;
    public Integer adminId;
    public String reason;
    public String status;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
}
