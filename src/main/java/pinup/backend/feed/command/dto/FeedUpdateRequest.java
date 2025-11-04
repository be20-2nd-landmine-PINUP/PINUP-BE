package pinup.backend.feed.command.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "피드 수정 요청 DTO")
public class FeedUpdateRequest {

    @Schema(description = "수정할 피드 제목", example = "수정할 제목아무말대잔치")
    private String title;

    @Schema(description = "수정할 피드 내용", example = "아아아아아에서 가가가가가로 경상도식 수정")
    private String content;

    @Schema(description = "수정할 이미지 URL", example = "https://backend.pinup/feed/{아직경로미정...}/anotherimage.jpg", nullable = true)
    private String imageUrl;

    public FeedUpdateRequest(String title, String content, String imageUrl) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }
}
