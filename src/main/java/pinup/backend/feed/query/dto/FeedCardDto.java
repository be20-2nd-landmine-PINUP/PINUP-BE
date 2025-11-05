package pinup.backend.feed.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Schema(description = "피드 카드 정보 (리스트용)")
public record FeedCardDto(
        @Schema(description = "피드 ID", example = "619")
        Long id,

        @Schema(description = "피드 제목", example = "예제 제목")
        String title,

        @Schema(description = "대표 이미지 URL (null 허용)", example = "https://backend.pinup/feed/{아직경로미정...}/image.jpg")
        String imageUrl,

        @Schema(description = "작성자 이름", example = "홍길동")
        String authorName,

        @Schema(description = "등록일시", example = "2025-11-05T14:23:00")
        LocalDateTime createdAt
) {


    // 현재 날짜에 따른 시각 표기법
    public String formattedCreatedAt() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("MM-dd");
        DateTimeFormatter yearFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (createdAt.toLocalDate().equals(now.toLocalDate())) {
            // 오늘이면 "시:분"
            return createdAt.format(timeFmt);
        } else if (createdAt.getYear() == now.getYear()) {
            // 올해면 "월-일"
            return createdAt.format(dateFmt);
        } else {
            // 작년 이전이면 "YYYY-MM-DD"
            return createdAt.format(yearFmt);
        }
    }
}
