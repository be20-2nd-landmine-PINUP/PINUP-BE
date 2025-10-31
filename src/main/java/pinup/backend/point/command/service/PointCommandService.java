package pinup.backend.point.command.service;

// point/command/service/PointCommandService.java
package com.example.project.point.command.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.point.command.domain.PointLog;
import pinup.backend.point.command.repository.PointLogRepository;
import pinup.backend.point.command.repository.TotalPointRepository;

@Service
public class PointCommandService {

    private final PointLogRepository pointLogRepository;
    private final TotalPointRepository totalPointRepository;

    public PointCommandService(PointLogRepository pointLogRepository,
                               TotalPointRepository totalPointRepository) {
        this.pointLogRepository = pointLogRepository;
        this.totalPointRepository = totalPointRepository;
    }

    @Transactional
    public void grant(Long userId, int value, Long sourceId, String sourceType) {
        // 로그
        pointLogRepository.save(new PointLog(userId, sourceId, SourceType.valueOf(sourceType), value));
        // 누적 (upsert add)
        totalPointRepository.upsertAdd(userId, value);
    }

    @Transactional
    public void use(Long userId, int value, Long itemId) {
        int affected = totalPointRepository.trySubtract(userId, value);
        if (affected == 0) {
            throw new IllegalStateException("포인트 부족");
        }
        // 차감 로그는 음수로 기록 (DDL 유지: source_type='STORE')
        pointLogRepository.save(new PointLog(userId, itemId, SourceType.STORE, -value));
    }
}
