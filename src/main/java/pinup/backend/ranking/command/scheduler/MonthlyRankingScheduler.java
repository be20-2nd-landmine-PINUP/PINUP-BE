package pinup.backend.ranking.command.scheduler;


import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pinup.backend.ranking.command.service.RankingCommandService;

import java.time.YearMonth;
import java.time.ZoneId;

/**
 * Scheduler: 매월 1일 00:05에 지난달 랭킹 자동 집계
 */
// 스케줄링 기능 활성화 어노테이션
// 이 클래스에서 @Scheduled가 동작하도록 Spring에 알림.
@EnableScheduling
// ✅ Spring Bean으로 등록되는 컴포넌트
// Scheduler는 일반적으로 @Component로 등록해 자동 실행되게 함.
@Component
@RequiredArgsConstructor
public class MonthlyRankingScheduler {
    // 랭킹 생성을 담당하는 서비스 의존성 주입
    private final RankingCommandService rankingCommandService;

    /**
     * 매월 1일 00:05 (Asia/Seoul 기준)에 지난달 랭킹 자동 생성
     - cron = "0 5 0 1 * *"
     *      → 초 분 시 일 월 요일
     *      → 매월 1일 00시 05분
     */
    @Scheduled(cron = "0 5 0 1 * *", zone = "Asia/Seoul")
    public void run() {
        // 현재 시간 기준으로 지난 달(전월) 계산
        YearMonth lastMonth = YearMonth.now(ZoneId.of("Asia/Seoul")).minusMonths(1);
        //지난달 랭킹을 생성한느 서비스 호출
        rankingCommandService.buildMonthlyRanking(lastMonth);
    }
}
/*
Spring Boot가 시작되면, @EnableScheduling 덕분에 스케줄러가 활성화됩니다.

매월 1일 00:05 (한국 시간) 이 되면 run() 메서드가 자동 호출됩니다.

현재 시간을 기준으로 **지난달(전월)**을 계산 (YearMonth.now().minusMonths(1))
→ 예: 2025년 11월 1일 00:05에 실행 → lastMonth = 2025-10

rankingCommandService.buildMonthlyRanking(lastMonth)를 호출해
해당 월의 랭킹 데이터를 자동으로 생성합니다.

서비스 내부에서는 데이터 집계, 정렬, DB 저장 등의 작업을 수행하게 됩니다.
 */