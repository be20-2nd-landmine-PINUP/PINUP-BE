package pinup.backend.notice.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class NoticeListResponse {
    private Integer noticeId;
    private String noticeTitle;
    private LocalDateTime createdAt;
}
