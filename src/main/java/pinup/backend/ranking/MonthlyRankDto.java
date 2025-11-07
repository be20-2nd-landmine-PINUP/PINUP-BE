package pinup.backend.ranking;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

// API 응답 DTO
@Getter
@Builder
public class MonthlyRankDto {
    private int rank;
    private Long userId;
    private String nickname;
    private long captureCount;
    private Instant lastCaptureAt;
}