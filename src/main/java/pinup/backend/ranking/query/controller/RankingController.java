package pinup.backend.ranking.query.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pinup.backend.ranking.query.service.RankingQueryService;
import pinup.backend.ranking.query.dto.MonthlyRankDto;
import pinup.backend.ranking.query.dto.MyRankDto;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/ranks")
@RequiredArgsConstructor
public class RankingController {

    private final RankingQueryService rankingQueryService;


    // 월간 랭킹 (동점 포함 Top100)
    @GetMapping("/monthly")
    public List<MonthlyRankDto> getMonthlyRank(
            @RequestParam int year,
            @RequestParam int month
    ) {
        return rankingQueryService.getMonthlyTop100WithTies(YearMonth.of(year, month));
    }

    // 내 순위 (100위 밖이면 "순위권 밖입니다.")
    @GetMapping("/monthly/me")
    public MyRankDto getMyMonthlyRank(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam Long userId
    ) {
        return rankingQueryService.getMyMonthlyRank(userId, YearMonth.of(year, month));
    }

}
