package pinup.backend.notice.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class NoticeListResponse {
    private Integer noticeId;
    private String noticeTitle;
}
