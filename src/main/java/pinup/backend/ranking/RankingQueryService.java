package pinup.backend.ranking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankingQueryService {

    private final RankingMapper rankingMapper;
    private static final ZoneId ZONE = ZoneId.of("Asia/Seoul");

    public List<MonthlyRankDto> getMonthlyTop100WithTies(YearMonth ym) {
        var start = ym.atDay(1).atStartOfDay(ZONE).toInstant();
        var end   = ym.plusMonths(1).atDay(1).atStartOfDay(ZONE).toInstant();
        return rankingMapper.selectMonthlyTop100WithTies(start, end);
    }

    public MyRankDto getMyMonthlyRank(Long userId, YearMonth ym) {
        var list = getMonthlyTop100WithTies(ym);
        return list.stream()
                .filter(r -> r.userId().equals(userId))
                .findFirst()
                .map(r -> new MyRankDto(userId, r.nickname(), r.captureCount(), r.rank(), "현재 " + r.rank() + "위입니다."))
                .orElseGet(() -> new MyRankDto(userId, "Unknown", null, null, "순위권 밖입니다."));
    }
}
