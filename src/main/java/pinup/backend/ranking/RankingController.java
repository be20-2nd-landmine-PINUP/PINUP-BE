package pinup.backend.ranking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/ranks")
@RequiredArgsConstructor
public class RankingController {

    private final RankingQueryService rankingQueryService;

    /**
     * 예:
     *  - 동점 포함 + 고유 지역 기준(기본):  GET /ranks/monthly?year=2025&month=11
     *  - 동점 미포함(정확히 100명만):    GET /ranks/monthly?year=2025&month=11&includeTies=false
     *  - (선택) 고유 지역이 아닌 전체 캡처수 기준은 서비스에 별도 메서드 추가 후 분기
     */
    @GetMapping("/monthly")
    public List<MonthlyRankDto> getMonthlyRank(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam(defaultValue = "true") boolean distinct,       // 고유 지역 기준 여부
            @RequestParam(defaultValue = "true") boolean includeTies     // 동점 포함 여부
    ) {
        YearMonth ym = YearMonth.of(year, month);

        return rankingQueryService.getMonthlyTop100WithTies(ym);
    }

}
