package pinup.backend.ranking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.conquer.command.domain.repository.TerritoryRepository;
import pinup.backend.member.command.domain.Users;
import pinup.backend.member.command.repository.UserRepository;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankingQueryService {

    private final TerritoryRepository territoryRepository;
    private final UserRepository userRepository; // 닉네임/프로필 조회용

    // 시스템 표준 타임존을 명시(서비스 정책에 맞게 조정) - 예: Asia/Seoul
    private static final ZoneId ZONE = ZoneId.of("Asia/Seoul");

    /**
     * 월간 Top100 조회 (고유 지역 수 기준, ✅동점 포함)
     * - 1) 100번째 컷오프 captureCount를 구한 뒤
     * - 2) 그 이상인 사용자 전부(HAVING >= cutoff)를 불러온다.
     * - 3) 같은 개수면 같은 순위(경쟁 랭킹: 1,2,2,4)로 표시한다.
     */
    @Transactional(readOnly = true)
    public List<MonthlyRankDto> getMonthlyTop100WithTies(YearMonth ym) {
        Date start = Date.from(ym.atDay(1).atStartOfDay(ZONE).toInstant());
        Date end   = Date.from(ym.plusMonths(1).atDay(1).atStartOfDay(ZONE).toInstant());

        // 1) 100번째 레코드로 컷오프 개수 구하기 (정렬은 레포 쿼리와 동일해야 함)
        List<MonthlyCaptureRankView> kth =
                territoryRepository.findMonthlyTop100DistinctRegion(start, end, PageRequest.of(99, 1));
        if (kth.isEmpty()) {
            return List.of();
        }
        long minCount = kth.getFirst().getCaptureCount();

        // 2) 컷오프 이상(동점 포함) 전부 조회
        List<MonthlyCaptureRankView> rows =
                territoryRepository.findMonthlyRankWithMinCount(start, end, minCount);

        // 3) 닉네임 일괄 로딩 (N+1 방지)
        Map<Long, String> nickById = fetchNicknames(
                rows.stream().map(MonthlyCaptureRankView::getUserId).collect(java.util.stream.Collectors.toSet())
        );

        // 4) 같은 개수면 같은 순위(경쟁 랭킹: 1,2,2,4)
        List<MonthlyRankDto> result = new ArrayList<>();
        long prevCount = Long.MIN_VALUE;
        int displayRank = 0;  // 표기될 순위
        int idx = 0;          // 전체 순번(1-base)

        for (MonthlyCaptureRankView r : rows) {
            idx++;
            if (r.getCaptureCount() != prevCount) {
                displayRank = idx;          // 새 카운트 등장 시 현재 순번이 곧 순위
                prevCount = r.getCaptureCount();
            }
            result.add(MonthlyRankDto.builder()
                    .rank(displayRank)
                    .userId(r.getUserId())
                    .nickname(nickById.getOrDefault(r.getUserId(), "Unknown"))
                    .captureCount(r.getCaptureCount())
                    .lastCaptureAt(r.getLastCaptureAt().toInstant())
                    .build());
        }
        return result;
    }

    private Map<Long, String> fetchNicknames(Set<Long> userIds) {
        // 예시: UserRepository에 findAllById 로딩 후 Map 변환
        return userRepository.findAllById(userIds).stream()
                .collect(java.util.stream.Collectors.toMap(
                        Users::getUserId,
                        u -> java.util.Optional.ofNullable(u.getNickname()).orElse("User" + u.getUserId())
                ));
    }


    @Transactional(readOnly = true)
    public MyRankDto getMyMonthlyRank(Long userId, YearMonth ym) {
        // 1) 전체 랭킹 조회 (Top100 + 동점 포함)
        List<MonthlyRankDto> ranks = getMonthlyTop100WithTies(ym);

        // 2) 내 순위 찾기 (NPE 방지 equals)
        return ranks.stream()
                .filter(r -> java.util.Objects.equals(r.getUserId(), userId))
                .findFirst()
                .map(r -> new MyRankDto(
                        r.getUserId(),
                        r.getNickname(),
                        r.getCaptureCount(),
                        r.getRank(),
                        "현재 " + r.getRank() + "위입니다."
                ))
                .orElseGet(() -> new MyRankDto(
                        userId,
                        userRepository.findById(userId)
                                .map(Users::getNickname)
                                .orElse("Unknown"),
                        null,       // 순위권 밖이면 점령 수 비노출 정책
                        null,       // 순위 없음
                        "순위권 밖입니다."
                ));
    }


}
