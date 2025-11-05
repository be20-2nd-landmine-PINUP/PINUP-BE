package pinup.backend.feed.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "피드 상세 조회 DTO")
public record FeedQueryDto(
        @Schema(description = "피드 ID", example = "619")
        Long id,

        @Schema(description = "작성자 닉네임", example = "waifuser")
        String authorName,

        @Schema(description = "작성자 프로필 이미지 URL", example = "https://backend.pinup/{프로필이미지어쩌고}.jpg")
        String authorProfileImage,

        @Schema(description = "피드 제목", example = "마 붓싼아이가?")
        String title,

        @Schema(description = "피드 내용", example = "붓싼에선 제목이 곧 내용이다")
        String content,

        @Schema(description = "피드 이미지 URL", example = "https://backend.pinup/feed/{아직경로미정...}/image.jpg")
        String imageUrl,

        @Schema(description = "좋아요 수", example = "42")
        int likeCount,

        @Schema(description = "작성 시각", example = "2025-11-05T14:32:00")
        LocalDateTime createdAt
) {}
