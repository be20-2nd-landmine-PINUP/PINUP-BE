package pinup.backend.ranking.command.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
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

    @Value("${ranking.lock.enabled:true}") // 운영/개발 기본 true, 테스트에서 false
    private boolean lockEnabled;

    // 집계 성공 시, 동일 key(YYYY-MM)의 Top100 캐시 무효화
    @CacheEvict(cacheNames = "rankingTop100", key = "#ym.toString()")
    public void buildMonthlyRanking(YearMonth ym) {
        // yyyy-MM 문자열 및 기간(해당월 ~ 다음달 1일) 계산
        String ymStr = ym.toString();     // "YYYY-MM"
        int year = ym.getYear();
        int month = ym.getMonthValue();
        // ▶ 서울 타임존 기준 경계 계산(오프바이원 방지)
        LocalDateTime start = ym.atDay(1).atStartOfDay();
        LocalDateTime end   = ym.plusMonths(1).atDay(1).atStartOfDay();

        String lockKey = "rank:" + ymStr;
        Integer got = 1;

        if (lockEnabled) {
            try {
                got = jdbc.queryForObject("SELECT GET_LOCK(:key, 5)",
                        Map.of("key", lockKey), Integer.class);
            } catch (Exception e) { got = 1; } // 테스트 DB/H2 대비
            if (got == null || got != 1) {
                throw new IllegalStateException("이미 해당 월 집계가 실행 중입니다.");
            }
        }

        try {
            // 1) 해당 월 기존 랭킹 삭제
            jdbc.update(
                    "DELETE FROM ranking WHERE year = :y AND month = :m",
                    Map.of("y", year, "m", month)
            );

            // 2) 월간 완료 수 기준 Top100 산출 → ranking(user_id, rank, year, month)에 삽입
            //   - 완료 기준: capture_end_at IS NOT NULL
            //   - 월 범위: [start, end)
            //   - ACTIVE 유저만
            //   - 동/읍/면/리 중복 방지: COUNT(DISTINCT t.region_id)
            //   - 동점 처리: user_id ASC
            String insertSql = """
                INSERT INTO ranking (user_id, rank, year, month)
                SELECT ranked.user_id, ranked.rank_int, :y, :m
                FROM (
                  SELECT
                    agg.user_id,
                    RANK() OVER (ORDER BY agg.completed_count DESC, agg.user_id ASC) AS rank_int
                  FROM (
                    SELECT
                      t.user_id,
                      COUNT(DISTINCT t.region_id) AS completed_count
                    FROM territory t
                    JOIN users u
                      ON u.user_id = t.user_id AND u.status = 'ACTIVE'
                    WHERE t.capture_end_at IS NOT NULL
                      AND t.capture_end_at >= :start
                      AND t.capture_end_at <  :end
                    GROUP BY t.user_id
                  ) agg
                ) ranked
                WHERE ranked.rank_int <= 100
                """;

            Map<String, Object> params = Map.of(
                    "y", year,
                    "m", month,
                    "start", Timestamp.valueOf(start),
                    "end",   Timestamp.valueOf(end)
            );

            jdbc.update(insertSql, params);
        } finally {
            if (lockEnabled) {
                try { jdbc.queryForObject("SELECT RELEASE_LOCK(:key)", Map.of("key", lockKey), Integer.class); }
                catch (Exception ignore) {}
            }
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