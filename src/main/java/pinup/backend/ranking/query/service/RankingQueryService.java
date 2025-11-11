package pinup.backend.ranking.query.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.member.command.domain.Users;
import pinup.backend.member.command.repository.UserRepository;
import pinup.backend.ranking.query.dto.MonthlyRankDto;
import pinup.backend.ranking.query.dto.MyRankDto;
import pinup.backend.ranking.query.mapper.RankingMapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankingQueryService {

    private final RankingMapper rankingMapper;
    private final UserRepository userRepository;

    private static final ZoneId ZONE = ZoneId.of("Asia/Seoul");

    public List<MonthlyRankDto> getMonthlyTop100WithTies(YearMonth ym) {
        LocalDateTime start = ym.atDay(1).atStartOfDay(ZONE).toLocalDateTime();
        LocalDateTime end   = ym.plusMonths(1).atDay(1).atStartOfDay(ZONE).toLocalDateTime();
        return rankingMapper.selectMonthlyTop100WithTies(start, end);
    }

    public MyRankDto getMyMonthlyRank(Long userId, YearMonth ym) {
        var list = getMonthlyTop100WithTies(ym);
        return list.stream()
                .filter(r -> Objects.equals(r.getUserId(), userId))
                .findFirst()
                .map(r -> new MyRankDto(
                        r.getUserId(), r.getNickname(), r.getCaptureCount(), r.getRank(),
                        "현재 " + r.getRank() + "위입니다."))
                .orElseGet(() -> new MyRankDto(
                        userId,
                        userRepository.findById(userId).map(Users::getNickname).orElse("Unknown"),
                        null, null, "순위권 밖입니다."));
    }
}