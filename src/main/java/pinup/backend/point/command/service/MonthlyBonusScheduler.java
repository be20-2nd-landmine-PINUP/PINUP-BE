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
    private final PointCommandService pointService; // // 포인트 지급 로직을 담당하는 서비스
    private final Clock clock; // 테스트/재현을 위한 주입 권장
    // 배치 단위 크기 (기본값 500)
    @Value("${point.bonus.batch-size:500}")
    private int batchSize;

    // 실제 지급 여부 (true면 포인트 지급 안 하고 dry-run만 수
    @Value("${point.bonus.dry-run:false}")
    private boolean dryRun;
    /**
     * 지역(depth1, depth2, depth3) 조합을 담는 내부 레코드 클래스 (Java 16+)
     * - SQL 결과 매핑용 간단한 DTO 역할
     */
    public MonthlyBonusScheduler(JdbcTemplate jdbc, PointCommandService pointService, Clock clock) {
        this.jdbc = jdbc;
        this.pointService = pointService;
        this.clock = clock;
    }

    // depth 조합을 담을 간단한 레코드(자바 16+)
    private record Area(String d1, String d2, String d3) {}

    // 매월 1일 00:05 KST 실행
    @Scheduled(cron = "${point.bonus.cron}", zone = "Asia/Seoul")
    public void grantMonthlyBonus() {
        ZoneId zone = ZoneId.of("Asia/Seoul");
        // 기준 월(지난달)을 계산
        YearMonth targetYm = YearMonth.from(ZonedDateTime.now(clock).withZoneSameInstant(zone)).minusMonths(1);
        int yearMonth = targetYm.getYear() * 100 + targetYm.getMonthValue(); // 예: 202510

        // 조회 구간 (지난달 1일 00:00 ~ 이번달 1일 00:00)
        Instant from = targetYm.atDay(1).atStartOfDay(zone).toInstant();
        Instant to   = targetYm.plusMonths(1).atDay(1).atStartOfDay(zone).toInstant();

        /**
         * 1단계: 방문 수가 100 이하인 지역(depth3 단위)을 조회하는 SQL
         * - 지난달 동안 방문(valid)한 지역 중, 100회 이하의 방문만 대상으로 함
         * - 배치 단위(batchSize)로 끊어서 OFFSET 처리
         */

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


        /**
         * 2단계: 특정 지역의 사용자 목록(user_id)을 조회하는 SQL
         * - 방문 기록에서 해당 지역(depth1,2,3)에 방문한 사용자만 추출
         * - 키셋 페이징(user_id > lastUserId)으로 효율적으로 페이지 처리
         */
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

        int offset = 0; // 지역 조회용 오프셋
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
            // 더 이상 조회할 지역이 없으면 루프 종료
            if (areas.isEmpty()) break;
            offset += areas.size();

            // 2) 각 지역별 사용자 조회 및 보너스 지급
            for (Area area : areas) {
                String d1 = area.d1();
                String d2 = area.d2();
                String d3 = area.d3();

                Long lastUserId = null; // 2) 사용자 키셋 페이징
                while (true) {
                    // 해당 지역의 사용자 목록 조회
                    List<Long> userIds = jdbc.query(
                            sqlUsers,
                            (rs, i) -> rs.getLong(1),
                            d1, d2, d3,
                            from, to,
                            lastUserId, lastUserId,
                            batchSize
                    );
                    if (userIds.isEmpty()) break;// 더 이상 사용자 없으면 다음 지역으로

                    for (Long userId : userIds) {
                        if (dryRun) {
                            // 필요시 실제 event_key 포맷으로 미리보기
                            // log.info("DRY-RUN monthly bonus (d1/d2/d3) user={}, {}|{}|{}, yyyymm={}",
                            //          userId, d1, d2, d3, yearMonth);
                        } else {
                            // 실제 포인트 지급 로직 호출
                            pointService.grantMonthlyBonusByDepth3(userId, d1, d2, d3, yearMonth);
                        }
                        lastUserId = userId; // 다음 페이지 조회를 위한 키셋 기준 갱신
                    }
                }
            }
        }
    }
}
//운영에서는 dryRun=false로 실행