package pinup.backend.point.command.service;

import org.apache.commons.lang3.tuple.Triple;
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

    // depth 조합을 담을 간단한 레코드(자바 16+)
    private record Area(String d1, String d2, String d3) {}

    // 매월 1일 00:05 KST
    @Scheduled(cron = "${point.bonus.cron}", zone = "Asia/Seoul")
    public void grantMonthlyBonus() {
        ZoneId zone = ZoneId.of("Asia/Seoul");
        YearMonth targetYm = YearMonth.from(ZonedDateTime.now(clock).withZoneSameInstant(zone)).minusMonths(1);
        int yearMonth = targetYm.getYear() * 100 + targetYm.getMonthValue(); // 예: 202510

        Instant from = targetYm.atDay(1).atStartOfDay(zone).toInstant();
        Instant to   = targetYm.plusMonths(1).atDay(1).atStartOfDay(zone).toInstant();

        final String sqlAreas = """
            SELECT r.region_depth1, r.region_depth2, r.region_depth3
              FROM region r
              JOIN territory t ON t.region_id = r.region_id
              JOIN territory_visit_log v ON v.territory_id = t.territory_id
             WHERE v.is_valid = TRUE
               AND v.visited_at >= ?
               AND v.visited_at <  ?
             GROUP BY r.region_depth1, r.region_depth2, r.region_depth3
            HAVING COUNT(*) <= 100
             ORDER BY r.region_depth1, r.region_depth2, r.region_depth3
             LIMIT ? OFFSET ?
        """;

        final String sqlUsers = """
            SELECT DISTINCT v.user_id
              FROM territory_visit_log v
              JOIN territory t ON t.territory_id = v.territory_id
              JOIN region r ON r.region_id = t.region_id
             WHERE r.region_depth1 = ?
               AND r.region_depth2 = ?
               AND r.region_depth3 = ?
               AND v.is_valid = TRUE
               AND v.visited_at >= ?
               AND v.visited_at <  ?
               AND ( ? IS NULL OR v.user_id > ? )   -- 키셋 페이징
             ORDER BY v.user_id
             LIMIT ?
        """;

        int offset = 0;
        while (true) {
            // 1) 지난달 방문 수 ≤ 100 인 (depth1, depth2, depth3) 목록 배치 조회
            List<Area> areas = jdbc.query(
                    sqlAreas,
                    (rs, i) -> new Area(
                            rs.getString(1),
                            rs.getString(2),
                            rs.getString(3)
                    ),
                    from, to, batchSize, offset
            );

            if (areas.isEmpty()) break;
            offset += areas.size();

            for (Area area : areas) {
                String d1 = area.d1();
                String d2 = area.d2();
                String d3 = area.d3();

                Long lastUserId = null; // 2) 사용자 키셋 페이징
                while (true) {
                    List<Long> userIds = jdbc.query(
                            sqlUsers,
                            (rs, i) -> rs.getLong(1),
                            d1, d2, d3,
                            from, to,
                            lastUserId, lastUserId,
                            batchSize
                    );
                    if (userIds.isEmpty()) break;

                    for (Long userId : userIds) {
                        if (dryRun) {
                            // 필요시 실제 event_key 포맷으로 미리보기
                            // log.info("DRY-RUN monthly bonus (d1/d2/d3) user={}, {}|{}|{}, yyyymm={}",
                            //          userId, d1, d2, d3, yearMonth);
                        } else {
                            pointService.grantMonthlyBonusByDepth3(userId, d1, d2, d3, yearMonth);
                        }
                        lastUserId = userId;
                    }
                }
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