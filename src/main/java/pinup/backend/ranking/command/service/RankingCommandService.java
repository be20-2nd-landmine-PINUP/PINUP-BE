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
@Service //spring bean 등록, 서비스 계층 역할
@RequiredArgsConstructor // final 필드 자동 생성자 주입
@Transactional // 클래스 단위 기본 트랜잭션 (readOnly=false)
public class RankingCommandService {
    // NamedParameterJdbcTemplate: 이름 기반 파라미터를 사용하는 JDBC 헬퍼
    //  JPA보다 복잡한 SQL(집계, 윈도우 함수 등)에 유용함
    private final NamedParameterJdbcTemplate jdbc;

    /**
     * 월별 랭킹을 재집계하여 user_monthly_territory_rank 테이블에 반영한다.
     * MySQL GET_LOCK을 사용해 월 단위 중복 실행을 방지.
     */
    public void buildMonthlyRanking(YearMonth ym) {
       // 락 키;  (예: "rank:2025-10")
        String lockKey = "rank:" + ym;

        // 1️⃣ 월별 집계 락 획득 시도
        // GET_LOCK(:key, 5) → 최대 5초 동안 해당 key에 대한 락을 획득 시도
        Integer got = jdbc.queryForObject(
                "SELECT GET_LOCK(:key, 5)",
                Map.of("key", lockKey),
                Integer.class
        );

        // 락 획득 실패 시 (이미 다른 프로세스가 실행 중)
        if (got == null || got != 1) {
            throw new IllegalStateException("이미 해당 월 집계가 실행 중입니다.");
        }

        try {
            // 2️⃣ 월 구간 계산 (Asia/Seoul 기준)
            ZoneId zone = ZoneId.of("Asia/Seoul");
            LocalDateTime start = ym.atDay(1).atStartOfDay(zone).toLocalDateTime();
            LocalDateTime end = ym.plusMonths(1).atDay(1).atStartOfDay(zone).toLocalDateTime();

            // 3️⃣ 월별 랭킹 계산 SQL
            /*WITH monthly: 월 단위로 각 사용자별 완료 횟수 집계
              - territory 테이블 기준
              - users.status='ACTIVE'인 사용자만 포함
              - 유효한 방문(visit_log.is_valid=TRUE)이 존재하는 지역만 카운트
                 INSERT INTO ... SELECT ...:
              - 집계 결과를 user_monthly_territory_rank 테이블에 저장
              - DENSE_RANK()로 순위를 계산 (동점자 순위 동일)
              - 중복 키가 존재하면 UPDATE로 갱신

             */
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
            // SQL에 바인딩할 파라미터 준비
            Map<String, Object> params = Map.of(
                    "start", Timestamp.valueOf(start),
                    "end", Timestamp.valueOf(end)
            );

            // 4️⃣ 랭킹 집계 sql 실행
            jdbc.update(sql, params);

        } finally {
            // 5️⃣ 락 해제 (결과는 무시)
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