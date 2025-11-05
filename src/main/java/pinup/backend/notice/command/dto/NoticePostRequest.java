package pinup.backend.notice.command.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class NoticePostRequest {
    private Long adminId;
    private String noticeTitle;
    private String noticeContent;
}
