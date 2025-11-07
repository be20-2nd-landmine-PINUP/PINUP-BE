package pinup.backend.ranking.query.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MonthlyRankDto {
    private final Integer rank;
    private final Long userId;
    private final String nickname;
    private final Long captureCount;
    private final java.time.Instant lastCaptureAt;
}
