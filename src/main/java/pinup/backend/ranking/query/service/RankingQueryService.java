package pinup.backend.ranking.query.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.ranking.query.dto.MyRankResponse;
import pinup.backend.ranking.query.dto.TopRankResponse;

import java.util.List;
import java.util.Map;

/**
 * 조회(Query) 전용 랭킹 서비스
 * - 캐시 + DB 조회 담당
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankingQueryService {

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * ✅ 상위 100위 캐시 조회 (없으면 DB에서 조회 후 캐시 저장)
     * 캐시 유효시간: CacheConfig.java 에서 설정 (기본 1분)
     */
    @Cacheable(cacheNames = "rankingTop100", key = "#ym")
    public List<TopRankResponse> getTop100(String ym) {
        String sql = """
            SELECT r.rank_int AS rank, r.user_id, u.user_name, r.completed_count
            FROM user_monthly_territory_rank r
            JOIN users u ON u.user_id = r.user_id
            WHERE r.year_month = :ym
              AND u.status = 'ACTIVE'
              AND r.rank_int <= 100
            ORDER BY r.rank_int, r.user_id
            """;

        return jdbc.query(sql, Map.of("ym", ym), (rs, rn) -> TopRankResponse.builder()
                .rank(rs.getInt("rank"))
                .userId(rs.getLong("user_id"))
                .userName(rs.getString("user_name"))
                .completedCount(rs.getInt("completed_count"))
                .build());
    }

    /**
     * ✅ 특정 연월 캐시 무효화 (Command 서비스에서 집계 완료 후 호출)
     */
    @CacheEvict(cacheNames = "rankingTop100", key = "#ym")
    public void evictTop100Cache(String ym) {
        // 내용 없음 — 애노테이션이 캐시 삭제를 자동 처리
    }

    /**
     * ✅ 내 순위 조회 (캐시 사용 X)
     */
    public MyRankResponse getMyRank(String ym, long userId) {
        String sql = """
            SELECT rank_int AS rank, completed_count
            FROM user_monthly_territory_rank
            WHERE year_month = :ym
              AND user_id    = :userId
            """;

        List<MyRankResponse> list = jdbc.query(
                sql,
                Map.of("ym", ym, "userId", userId),
                (rs, rowNum) -> MyRankResponse.builder()
                        .rank(rs.getObject("rank", Integer.class))
                        .completedCount(rs.getObject("completed_count", Integer.class))
                        .message(null)
                        .build()
        );

        // 데이터 없을 경우
        if (list.isEmpty()) {
            return MyRankResponse.builder()
                    .rank(null)
                    .completedCount(0)
                    .message("해당 월 점령 완료 기록이 없습니다.")
                    .build();
        }

        MyRankResponse r = list.get(0);
        if (r.getRank() != null && r.getRank() > 100) {
            return MyRankResponse.builder()
                    .rank(r.getRank())
                    .completedCount(r.getCompletedCount())
                    .message("순위권 밖에 있습니다.")
                    .build();
        }
        return r;
    }
}
