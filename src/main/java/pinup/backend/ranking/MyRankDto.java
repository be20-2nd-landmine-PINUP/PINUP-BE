package pinup.backend.ranking;
import lombok.Getter;

@Getter
public class MyRankDto {
    private final Long userId;
    private final String nickname;
    private final Long captureCount;
    private final Integer rank;
    private final String message;

    public MyRankDto(Long userId, String nickname, Long captureCount, Integer rank, String message) {
        this.userId = userId;
        this.nickname = nickname;
        this.captureCount = captureCount;
        this.rank = rank;
        this.message = message;
    }
}
