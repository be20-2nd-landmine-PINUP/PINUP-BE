package pinup.backend.feed.command.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "피드 작성 요청 DTO")
public class FeedCreateRequest {

    @Schema(description = "작성자 고유식별자", example = "1")
    private Long userId;

    @Schema(description = "피드 제목", example = "피드 작성용 예시제목")
    private String title;

    @Schema(description = "피드 내용", example = "내용 예시 아아아아아아아")
    private String content;

    @Schema(description = "첨부 이미지 URL", example = "https://backend.pinup/feed/{아직경로미정...}/image.jpg", nullable = true)
    private String imageUrl;

    // 빌더
    public FeedCreateRequest(Long userId, String title, String content, String imageUrl) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }
}