package pinup.backend.ranking;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import pinup.backend.ranking.query.dto.MyRankResponse;
import pinup.backend.ranking.query.service.RankingQueryService;
import static org.assertj.core.api.Assertions.assertThat;


class RankingQueryServiceMyRankSimpleTest {

    @Test
    void myRank_noRecord_and_completedZero() {
        NamedParameterJdbcTemplate jdbc = Mockito.mock(NamedParameterJdbcTemplate.class);

        // rank 조회: 빈 리스트
        Mockito.when(jdbc.query(
                ArgumentMatchers.startsWith("SELECT r.rank"),
                ArgumentMatchers.anyMap(),
                ArgumentMatchers.<RowMapper<Integer>>any()
        )).thenReturn(java.util.Collections.emptyList());

        // completed 조회: 0 반환
        Mockito.when(jdbc.query(
                // ✅ region_id로 변경 + 앞 공백 대비해 contains 사용
                ArgumentMatchers.contains("SELECT COUNT(DISTINCT t.region_id)"),
                ArgumentMatchers.anyMap(),
                ArgumentMatchers.<RowMapper<Integer>>any()
        )).thenReturn(java.util.List.of(0));

        RankingQueryService svc = new RankingQueryService(jdbc);
        MyRankResponse r = svc.getMyRank("2025-10", 42L);

        assertThat(r.getRank()).isNull();
        assertThat(r.getCompletedCount()).isEqualTo(0);
        assertThat(r.getMessage()).isEqualTo("해당 월 점령 완료 기록이 없습니다.");
    }

    @Test
    void myRank_noRecord_and_completedPositive() {
        NamedParameterJdbcTemplate jdbc = Mockito.mock(NamedParameterJdbcTemplate.class);

        Mockito.when(jdbc.query(
                ArgumentMatchers.startsWith("SELECT r.rank"),
                ArgumentMatchers.anyMap(),
                ArgumentMatchers.<RowMapper<Integer>>any()
        )).thenReturn(java.util.Collections.emptyList());

        Mockito.when(jdbc.query(
                // ✅ region_id + contains
                ArgumentMatchers.contains("SELECT COUNT(DISTINCT t.region_id)"),
                ArgumentMatchers.anyMap(),
                ArgumentMatchers.<RowMapper<Integer>>any()
        )).thenReturn(java.util.List.of(3));

        RankingQueryService svc = new RankingQueryService(jdbc);
        MyRankResponse r = svc.getMyRank("2025-10", 42L);

        // (디버그 로그는 원하면 유지)
        System.out.println("=== 테스트 실행 ===");
        System.out.println("UserId: " + 42L);
        System.out.println("Rank: " + r.getRank());
        System.out.println("CompletedCount: " + r.getCompletedCount());
        System.out.println("Message: " + r.getMessage());
        System.out.println("==================");

        assertThat(r.getRank()).isNull();
        assertThat(r.getCompletedCount()).isEqualTo(3);
        assertThat(r.getMessage()).isEqualTo("순위권 밖에 있습니다.");
    }

    @Test
    void myRank_rankExists_messageNull() {
        NamedParameterJdbcTemplate jdbc = Mockito.mock(NamedParameterJdbcTemplate.class);

        // rank 조회: 55위라고 가정
        Mockito.when(jdbc.query(
                ArgumentMatchers.startsWith("SELECT r.rank"),
                ArgumentMatchers.anyMap(),
                ArgumentMatchers.<RowMapper<Integer>>any()
        )).thenReturn(java.util.List.of(55));

        // completed 조회: 2개라고 가정
        Mockito.when(jdbc.query(
                // ✅ region_id + contains
                ArgumentMatchers.contains("SELECT COUNT(DISTINCT t.region_id)"),
                ArgumentMatchers.anyMap(),
                ArgumentMatchers.<RowMapper<Integer>>any()
        )).thenReturn(java.util.List.of(2));

        RankingQueryService svc = new RankingQueryService(jdbc);
        MyRankResponse r = svc.getMyRank("2025-10", 42L);

        assertThat(r.getRank()).isEqualTo(55);
        assertThat(r.getCompletedCount()).isEqualTo(2);
        assertThat(r.getMessage()).isNull();
    }
}
