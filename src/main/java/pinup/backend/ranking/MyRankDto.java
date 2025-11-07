package pinup.backend.ranking;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyRankDto {
    private final Long userId;
    private final String nickname;
    private final Long captureCount;
    private final Integer rank;
    private final String message;


}
