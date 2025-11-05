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
/*
목적; 월간 랭킹 리스트 화면에 보여질 정보
PI에서 반환되는 랭킹 1위~100위 각각의 항목 구조를 정의하는 클래스
JSON 응답 예시
[
  {
    "rank": 1,
    "userId": 1001,
    "userName": "홍길동",
    "completedCount": 87
  },
  {
    "rank": 2,
    "userId": 1002,
    "userName": "김민수",
    "completedCount": 84
  },
  {
    "rank": 3,
    "userId": 1003,
    "userName": "이유정",
    "completedCount": 82
  }
]

 */