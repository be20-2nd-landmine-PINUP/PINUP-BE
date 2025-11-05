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
@Transactional
public class RankingCommandService {

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * 월별 랭킹을 재집계하여 ranking(user_id, rank, year, month)에 반영한다.
     * MySQL GET_LOCK으로 월 단위 중복 실행 방지.
     */
    public void buildMonthlyRanking(YearMonth ym) {
        String lockKey = "rank:" + ym;

        Integer got = jdbc.queryForObject(
                "SELECT GET_LOCK(:key, 5)",
                Map.of("key", lockKey),
                Integer.class
        );
        if (got == null || got != 1) {
            throw new IllegalStateException("이미 해당 월 집계가 실행 중입니다.");
        }

        try {
            ZoneId zone = ZoneId.of("Asia/Seoul");
            LocalDateTime start = ym.atDay(1).atStartOfDay(zone).toLocalDateTime();
            LocalDateTime end = ym.plusMonths(1).atDay(1).atStartOfDay(zone).toLocalDateTime();

            // 1) 해당 월 기존 랭킹 제거 (스키마 특성상 남아있으면 안 됨)
            jdbc.update(
                    "DELETE FROM ranking WHERE year = :year AND month = :month",
                    Map.of("year", ym.getYear(), "month", ym.getMonthValue())
            );

            // 2) 월별 completed_count 계산 + DENSE_RANK() 흉내(세션 변수) → ranking에 삽입
            // RankingCommandService.buildMonthlyRanking(...)
            String sql = """
    INSERT INTO ranking (user_id, rank, year, month)
    SELECT user_id, rnk, :year, :month
    FROM (
        SELECT
            t.user_id,
            DENSE_RANK() OVER (ORDER BY COUNT(DISTINCT t.region_code) DESC, t.user_id ASC) AS rnk
        FROM territory t
        JOIN users  u  ON u.user_id = t.user_id AND u.status = 'ACTIVE'
        JOIN region rg ON rg.region_code = t.region_code
        WHERE t.capture_end_at >= :start
          AND t.capture_end_at  < :end
          AND rg.region_depth3 IS NOT NULL        -- 동/읍/면만 집계
          AND EXISTS (
              SELECT 1 FROM territory_visit_log v
              WHERE v.territory_id = t.territory_id
                AND v.is_valid = TRUE
          )
        GROUP BY t.user_id
    ) ranked
    WHERE rnk <= 100
    """;


            Map<String, Object> params = Map.of(
                    "start", Timestamp.valueOf(start),
                    "end",   Timestamp.valueOf(end),
                    "year",  ym.getYear(),
                    "month", ym.getMonthValue()
            );

            jdbc.update(sql, params);

        } finally {
            jdbc.queryForObject("SELECT RELEASE_LOCK(:key)", Map.of("key", lockKey), Integer.class);
        }
    }
}

/*
주요 기술 포인트
1) @Transactional; 전체 메서드를 하나의 트랜잭션으로 묶어 원자성 보장
2) NamedParameterJdbcTemplate: 복잡한 SQL도 안전하게 파라미터 바인딩 가능
3) MySQL GET_LOCK() / RELEASE_LOCK(); 같은 월에 대한 중복 랭킹 생성 방지 (분산락 효과)
4) DENSE_RANK(); 중복 순위 건너뛰지 않고 계산
5) ON DUPLICATE KEY UPDATE; 	기존 데이터가 있을 경우 덮어쓰기 (갱신 처리)
 */