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
@EnableScheduling
@Component
@RequiredArgsConstructor
public class MonthlyRankingScheduler {

    private final RankingCommandService rankingCommandService;

    /**
     * 매월 1일 00:05 (Asia/Seoul 기준)에 지난달 랭킹 자동 생성
     */
    @Scheduled(cron = "0 5 0 1 * *", zone = "Asia/Seoul")
    public void run() {
        YearMonth lastMonth = YearMonth.now(ZoneId.of("Asia/Seoul")).minusMonths(1);
        rankingCommandService.buildMonthlyRanking(lastMonth);
    }
}
