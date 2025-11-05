package pinup.backend.ranking.query.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.ranking.query.dto.MyRankResponse;
import pinup.backend.ranking.query.dto.TopRankResponse;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
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

    /** 상위 100위: 랭킹은 ranking 테이블, 완료 개수는 조회 시 계산 */
    @Cacheable(cacheNames = "rankingTop100", key = "#ym")
    public List<TopRankResponse> getTop100(String ym) {
        YearMonth y = YearMonth.parse(ym);
        ZoneId zone = ZoneId.of("Asia/Seoul");
        LocalDateTime start = y.atDay(1).atStartOfDay(zone).toLocalDateTime();
        LocalDateTime end = y.plusMonths(1).atDay(1).atStartOfDay(zone).toLocalDateTime();

        String sql = """
            WITH counts AS (
                SELECT
                  t.user_id,
                  COUNT(DISTINCT t.region_code) AS completed_count
                FROM territory t
                JOIN users u ON u.user_id = t.user_id AND u.status = 'ACTIVE'
                WHERE t.capture_end_at >= :start
                  AND t.capture_end_at  < :end
                  AND EXISTS (
                    SELECT 1 FROM territory_visit_log v
                    WHERE v.territory_id = t.territory_id
                      AND v.is_valid = TRUE
                  )
                GROUP BY t.user_id
            )
            SELECT
              r.rank         AS rank,
              r.user_id      AS user_id,
              u.user_name    AS user_name,
              COALESCE(c.completed_count, 0) AS completed_count
            FROM ranking r
            JOIN users u   ON u.user_id = r.user_id AND u.status = 'ACTIVE'
            LEFT JOIN counts c ON c.user_id = r.user_id
            WHERE r.year = :year AND r.month = :month
            ORDER BY r.rank ASC, r.user_id ASC
            LIMIT 100
            """;

        Map<String, Object> params = Map.of(
                "start", Timestamp.valueOf(start),
                "end",   Timestamp.valueOf(end),
                "year",  y.getYear(),
                "month", y.getMonthValue()
        );

        return jdbc.query(sql, params, (rs, rn) -> TopRankResponse.builder()
                .rank(rs.getInt("rank"))
                .userId(rs.getLong("user_id"))
                .userName(rs.getString("user_name"))
                .completedCount(rs.getInt("completed_count"))
                .build());
    }

    /** 캐시 무효화 (Command 완료 후 호출) */
    @CacheEvict(cacheNames = "rankingTop100", key = "#ym")
    public void evictTop100Cache(String ym) {
        // 애노테이션이 처리
    }

    /** 내 순위: ranking에서 랭크 읽고, 완료 개수는 해당 월 계산 */
    public MyRankResponse getMyRank(String ym, long userId) {
        YearMonth y = YearMonth.parse(ym);
        ZoneId zone = ZoneId.of("Asia/Seoul");
        LocalDateTime start = y.atDay(1).atStartOfDay(zone).toLocalDateTime();
        LocalDateTime end = y.plusMonths(1).atDay(1).atStartOfDay(zone).toLocalDateTime();

        // rank 읽기
        Integer rank = jdbc.query(
                "SELECT r.rank FROM ranking r WHERE r.year=:year AND r.month=:month AND r.user_id=:uid",
                Map.of("year", y.getYear(), "month", y.getMonthValue(), "uid", userId),
                (rs, rn) -> rs.getInt("rank")
        ).stream().findFirst().orElse(null);

        // completed_count 계산
        Integer completed = jdbc.query(
                """
                SELECT COUNT(DISTINCT t.region_code) AS cnt
                FROM territory t
                JOIN users u ON u.user_id = t.user_id AND u.status = 'ACTIVE'
                WHERE t.user_id = :uid
                  AND t.capture_end_at >= :start
                  AND t.capture_end_at  < :end
                  AND EXISTS (
                    SELECT 1 FROM territory_visit_log v
                    WHERE v.territory_id = t.territory_id
                      AND v.is_valid = TRUE
                  )
                """,
                Map.of("uid", userId,
                        "start", Timestamp.valueOf(start),
                        "end", Timestamp.valueOf(end)),
                (rs, rn) -> rs.getInt("cnt")
        ).stream().findFirst().orElse(0);

        // RankingQueryService.getMyRank(...)
        if (rank == null) {
            if (completed == 0) {
                return MyRankResponse.builder()
                        .rank(null)
                        .completedCount(0)
                        .message("해당 월 점령 완료 기록이 없습니다.")
                        .build();
            } else {
                return MyRankResponse.builder()
                        .rank(null)
                        .completedCount(completed)
                        .message("순위권 밖에 있습니다.")
                        .build();
            }
        }

// (rank != null) 케이스
        return MyRankResponse.builder()
                .rank(rank)
                .completedCount(completed)
                .message(null)    // TOP100 안이므로 메시지 없음
                .build();

    }
}

/*
목적: “월간 랭킹 데이터를 캐싱하며 빠르게 조회”하는 역할을 하고,
“내 순위 조회는 항상 최신값을 DB에서 가져오는 구조”

JSON 응답 예시
{ "rank": 12, "completedCount": 35 }
{ "rank": 150, "completedCount": 20, "message": "순위권 밖에 있습니다." }
{ "completedCount": 0, "message": "해당 월 점령 완료 기록이 없습니다." }
 */