package pinup.backend.point.command.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
@Component
public class MonthlyBonusScheduler {

    private final JdbcTemplate jdbc;
    private final PointCommandService pointService;
    private final Clock clock; // 테스트/재현을 위한 주입 권장

    @Value("${point.bonus.batch-size:500}")
    private int batchSize;

    @Value("${point.bonus.dry-run:false}")
    private boolean dryRun;

    public MonthlyBonusScheduler(JdbcTemplate jdbc, PointCommandService pointService, Clock clock) {
        this.jdbc = jdbc;
        this.pointService = pointService;
        this.clock = clock;
    }

    // 매월 1일 00:05 KST
    @Scheduled(cron = "${point.bonus.cron}", zone = "Asia/Seoul")
    public void grantMonthlyBonus() {
        ZoneId zone = ZoneId.of("Asia/Seoul");
        YearMonth targetYm = YearMonth.from(ZonedDateTime.now(clock).withZoneSameInstant(zone)).minusMonths(1);
        long monthlyKey = targetYm.getYear() * 100L + targetYm.getMonthValue(); // 예: 202510

        Instant from = targetYm.atDay(1).atStartOfDay(zone).toInstant();
        Instant to   = targetYm.plusMonths(1).atDay(1).atStartOfDay(zone).toInstant();

        Long lastUserId = null;      // 키셋 페이징용
        Long lastTerritoryId = null; // 키셋 페이징용

        while (true) {
            // 지난달 유효 방문 수 ≤ 100인 (user_id, territory_id) 페어를 키셋 페이징으로 조회
            List<Pair<Long, Long>> rows = jdbc.query("""
                SELECT t.user_id, t.territory_id
                  FROM territory t
                 WHERE (
                    SELECT COUNT(*)
                      FROM territory_visit_log v
                     WHERE v.territory_id = t.territory_id
                       AND v.is_valid = TRUE
                       AND v.visited_at >= ?
                       AND v.visited_at <  ?
                 ) <= 100
                   AND (
                        ? IS NULL
                        OR (t.user_id > ?)
                        OR (t.user_id = ? AND t.territory_id > ?)
                   )
                 ORDER BY t.user_id, t.territory_id
                 LIMIT ?
            """, (rs, i) -> Pair.of(rs.getLong(1), rs.getLong(2)),
                    from, to,
                    lastUserId, lastUserId, lastUserId, lastTerritoryId,
                    batchSize);

            if (rows.isEmpty()) break;

            for (Pair<Long, Long> row : rows) {
                Long userId = row.getLeft();
                Long territoryId = row.getRight();

                if (dryRun) {
                    // 드라이런이면 로그만
                    // log.info("DRY-RUN bonus target user={}, territory={}, month={}", userId, territoryId, monthlyKey);
                } else {
                    // B안: 영토당 월1회 부여
                    pointService.grantMonthlyBonus(userId, territoryId, monthlyKey);
                }

                lastUserId = userId;
                lastTerritoryId = territoryId;
            }
        }
    }
}

/*



* source_type='CAPTURE' & source_id=0 고정으로 재 실행시 중복 적립 위험이 있어,
* source_id에 월 키(YYYYMM)를 넣는 것만으로 멱등
== 즉, 이렇게 하면 같은 달에 스케줄러가 몇 번 돌더라도 (userId, CAPTURE, YYYYMM) 조합이 이미 있으므로 선조회 단계에서 바로 스킵됩니다.

* 멱등 처리:

(userId, sourceType, sourceId)로 선조회 → named lock(GET_LOCK) → 재확인 → 처리 패턴이면, DDL 변경 없이 동시요청까지 안전하게 막습니다.

보너스 스케줄러 중복 방지:

sourceId = YYYYMM 같은 월 키로 기록하면, 같은 달에 재실행돼도 선조회에서 스킵되어 중복 적립이 안 됩니다.

 */