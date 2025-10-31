package pinup.backend.ranking.command.service;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Map;

/**
 * 쓰기(Command) 전용 랭킹 서비스
 * - 월별 랭킹 집계 / 재계산 등 데이터 변경 작업 수행
 */
@Service
@RequiredArgsConstructor
@Transactional // 클래스 단위 기본 트랜잭션 (readOnly=false)
public class RankingCommandService {

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * 월별 랭킹을 재집계하여 user_monthly_territory_rank 테이블에 반영한다.
     * MySQL GET_LOCK을 사용해 월 단위 중복 실행을 방지.
     */
    public void buildMonthlyRanking(YearMonth ym) {
        String lockKey = "rank:" + ym;

        // 1️⃣ 월별 집계 락 획득 시도
        Integer got = jdbc.queryForObject(
                "SELECT GET_LOCK(:key, 5)",
                Map.of("key", lockKey),
                Integer.class
        );
        if (got == null || got != 1) {
            throw new IllegalStateException("이미 해당 월 집계가 실행 중입니다.");
        }

        try {
            // 2️⃣ 월 구간 계산 (Asia/Seoul 기준)
            ZoneId zone = ZoneId.of("Asia/Seoul");
            LocalDateTime start = ym.atDay(1).atStartOfDay(zone).toLocalDateTime();
            LocalDateTime end = ym.plusMonths(1).atDay(1).atStartOfDay(zone).toLocalDateTime();

            // 3️⃣ 월별 랭킹 계산 SQL
            String sql = """
                WITH monthly AS (
                  SELECT DATE_FORMAT(t.capture_end_at, '%Y-%m') AS ym,
                         t.user_id AS user_id,
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
                  GROUP BY ym, t.user_id
                )
                INSERT INTO user_monthly_territory_rank (year_month, user_id, completed_count, rank_int)
                SELECT ym, user_id, completed_count,
                       DENSE_RANK() OVER (PARTITION BY ym ORDER BY completed_count DESC, user_id ASC) AS rnk
                FROM monthly
                ON DUPLICATE KEY UPDATE
                  completed_count = VALUES(completed_count),
                  rank_int        = VALUES(rank_int),
                  created_at      = CURRENT_TIMESTAMP
                """;

            Map<String, Object> params = Map.of(
                    "start", Timestamp.valueOf(start),
                    "end", Timestamp.valueOf(end)
            );

            // 4️⃣ 랭킹 집계 실행
            jdbc.update(sql, params);

        } finally {
            // 5️⃣ 락 해제 (결과는 무시)
            jdbc.queryForObject("SELECT RELEASE_LOCK(:key)", Map.of("key", lockKey), Integer.class);
        }
    }
}
