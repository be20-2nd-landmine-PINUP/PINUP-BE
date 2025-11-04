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
    public Integer report_id;
    public Integer user_id;
    public Integer feed_id;
    public Integer admin_id;
    public String reason;
    public String status;
    public LocalDateTime created_at;
    public LocalDateTime updated_at;
}
