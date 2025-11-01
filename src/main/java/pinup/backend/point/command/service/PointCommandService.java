package pinup.backend.point.command.service;

import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.point.command.domain.PointLog;
import pinup.backend.point.command.repository.PointLogRepository;
import pinup.backend.point.command.repository.TotalPointRepository;

// 포인트 적립, 차감하는 기능; 쓰기 전용
@Service
public class PointCommandService {
    // final은 이 서비스는 이 2개의 리포지토리가 필요하다는 의미.
    // 생성장에 리포를 주입한다.
    private final PointLogRepository pointLogRepository;
    private final TotalPointRepository totalPointRepository;

    public PointCommandService(PointLogRepository pointLogRepository,
                               TotalPointRepository totalPointRepository) {
        this.pointLogRepository = pointLogRepository;
        this.totalPointRepository = totalPointRepository;
    }
    // 포인트 적립; 좋아요, 점령, 보너스 + 내역 기록
    // 로그 저장과 포인트 합산은 하나의 논리적 작업. 둘 다 성공해야 커밋
    @Transactional
    public void grant(Long userId, int value, Long sourceId, String sourceType) {
        // point_log에 로그 저장
        pointLogRepository.save(new PointLog(userId, sourceId, PointLog.SourceType.valueOf(sourceType), value));
        // total_point에 합산 (upsert 계정 없으면 만들고, 아니면 누적)
        totalPointRepository.upsertAdd(userId, value);
    }
    // 포인트 차감; 상점 결제용

    @Transactional
    public void use(Long userId, int value, Long itemId) {
        int affected = totalPointRepository.trySubtract(userId, value); // trySubtract()쿼리 실행
        if (affected == 0) { // 포인트 부족; affected=0, 그 외; affected= 1
            throw new IllegalStateException("포인트 부족"); // 트랜젝션 롤백으로 로그 저장 안됨
        }
        // 차감 로그는 음수로 기록 (DDL 유지: source_type='STORE')
        pointLogRepository.save(new PointLog(userId, itemId, PointLog.SourceType.STORE, -value));
    }
}
