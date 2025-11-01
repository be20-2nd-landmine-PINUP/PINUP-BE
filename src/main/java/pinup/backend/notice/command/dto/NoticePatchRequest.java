package pinup.backend.notice.command.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class NoticePatchRequest {
    private String noticeTitle;
    private String noticeContent;
}
