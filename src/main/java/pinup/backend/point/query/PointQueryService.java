package pinup.backend.point.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.point.command.domain.TotalPoint;
import pinup.backend.point.command.repository.TotalPointRepository;

@Service
@Transactional(readOnly = true)
public class PointQueryService {

    private final TotalPointRepository totalPointRepository;
    public PointQueryService(TotalPointRepository totalPointRepository) {
        this.totalPointRepository = totalPointRepository;
    }
    /**
     * ✅ 유저의 누적 포인트(잔액) 조회
     *  - 존재하지 않으면 0으로 반환
     */
    public int getUserTotalPoint(Long userId) {
        return totalPointRepository.findByUserId(userId)
                .map(TotalPoint::getTotalPoint)
                .orElse(0);
    }
}

