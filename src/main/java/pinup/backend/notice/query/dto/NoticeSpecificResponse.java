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
public class NoticeSpecificResponse {
    private Integer noticeId;
    private Integer adminId;
    private String noticeTitle;
    private String noticeContent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
