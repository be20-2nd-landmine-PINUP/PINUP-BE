package pinup.backend.point.command.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.List;

@Component
public class MonthlyBonusScheduler {

    private final JdbcTemplate jdbc;
    private final PointCommandService pointService;

    @Value("${point.bonus.batch-size:500}")
    private int batchSize;

    public MonthlyBonusScheduler(JdbcTemplate jdbc, PointCommandService pointService) {
        this.jdbc = jdbc;
        this.pointService = pointService;
    }

    // 매월 1일 00:05 KST; 시간 기준 맞춰야함
    @Scheduled(cron = "${point.bonus.cron}", zone = "Asia/Seoul")
    public void grantMonthlyBonus() {
        ZoneId zone = ZoneId.of("Asia/Seoul");
        YearMonth targetYm = YearMonth.now(zone).minusMonths(1);
        long monthlyKey = targetYm.getYear() * 100L + targetYm.getMonthValue(); // 예: 202510

        var from = targetYm.atDay(1).atStartOfDay();
        var to   = targetYm.plusMonths(1).atDay(1).atStartOfDay();

        int offset = 0;
        while (true) {
            // 전월 유효 방문 수 ≤ 100인 territory 소유자 user_id 목록 페이징 조회
            List<Long> userIds = jdbc.query("""
                SELECT DISTINCT t.user_id
                  FROM territory t
                 WHERE (
                    SELECT COUNT(*) FROM territory_visit_log v
                     WHERE v.territory_id = t.territory_id
                       AND v.is_valid = TRUE
                       AND v.visited_at >= ?
                       AND v.visited_at <  ?
                 ) <= 100
                 ORDER BY t.user_id
                 LIMIT ? OFFSET ?
            """, (rs, i) -> rs.getLong(1),
                    from, to, batchSize, offset);

            if (userIds.isEmpty()) break;

            for (Long userId : userIds) {
                // 보너스는 CAPTURE 타입 + source_id=YYYYMM (멱등 키)
                pointService.grantMonthlyBonus(userId, monthlyKey);
            }
            offset += userIds.size();
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