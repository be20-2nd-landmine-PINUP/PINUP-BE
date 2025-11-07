package pinup.backend.point.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.point.command.domain.PointLog;
import pinup.backend.point.command.domain.TotalPoint;
import pinup.backend.point.command.repository.PointLogRepository;
import pinup.backend.point.command.repository.TotalPointRepository;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PointQueryService {

    private final TotalPointRepository totalPointRepository;
    public PointQueryService(TotalPointRepository totalPointRepository, PointLogRepository pointLogRepository) {
        this.totalPointRepository = totalPointRepository;
        this.pointLogRepository = pointLogRepository;
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
    private final PointLogRepository pointLogRepository;

    /**
     * ✅ 특정 유저의 포인트 로그 전체 조회
     * - 최신순으로 반환
     */
    public List<PointLog> getUserPointLogs(Long userId) {
        return pointLogRepository.findByUser_UserIdOrderByCreatedAtDesc(userId);
    }

}