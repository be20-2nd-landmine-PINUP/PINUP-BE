package pinup.backend.ranking.query.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.Builder;
import lombok.Getter;

/** 내 순위 조회 응답 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyRankResponse {

    /** 서버 계산 필드(읽기 전용) */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final Integer rank;            // null 허용

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final Integer completedCount;  // 0 가능

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final String message;          // 예: "순위권 밖입니다."

    /** 내부 검증용 식별자(외부 비노출) */
    @JsonIgnore
    private final Long ownerUserId;

    @Builder
    private MyRankResponse(Integer rank, Integer completedCount, String message, Long ownerUserId) {
        this.rank = rank;
        this.completedCount = completedCount;
        this.message = message;
        this.ownerUserId = ownerUserId;
    }

    // 편의 팩토리
    public static MyRankResponse of(Integer rank, Integer completedCount, String message) {
        return MyRankResponse.builder()
                .rank(rank)
                .completedCount(completedCount)
                .message(message)
                .build();
    }
}
