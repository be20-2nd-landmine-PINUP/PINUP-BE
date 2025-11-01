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
        LocalDate firstDayThisMonth = LocalDate.now(zone).withDayOfMonth(1);
        LocalDate firstDayPrevMonth = firstDayThisMonth.minusMonths(1);
        var from = firstDayPrevMonth.atStartOfDay();
        var to = firstDayThisMonth.atStartOfDay();

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
            """, (rs, i) -> rs.getLong(1), from, to, batchSize, offset);

            if (userIds.isEmpty()) break;

            for (Long userId : userIds) {
                // DDL 유지: 보너스는 source_type='CAPTURE' & source_id=0 으로 기록
                pointService.grant(userId, 10, 0L, "CAPTURE");
            }
            offset += userIds.size();
        }
    }
}
