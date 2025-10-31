package pinup.backend.ranking.query.dto;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

/** 상위 랭킹 목록 응답 아이템 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopRankResponse {
    private final Integer rank;
    private final Long userId;
    private final String userName;
    private final Integer completedCount;
}
