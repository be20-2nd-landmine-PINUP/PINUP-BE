package pinup.backend.point.command.service;

import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.point.command.domain.PointLog;
import pinup.backend.point.command.repository.PointLogRepository;
import pinup.backend.point.command.repository.TotalPointRepository;

import java.sql.PreparedStatement;

// 포인트 적립, 차감하는 기능; 쓰기 전용
// 중복 체크 -> named lock ->재확인 -> 처리
@Service
public class PointCommandService {
    // final은 이 서비스는 이 2개의 리포지토리가 필요하다는 의미.
    // 생성장에 리포를 주입한다.
    private final PointLogRepository pointLogRepository;
    private final TotalPointRepository totalPointRepository;
    private final JdbcTemplate jdbc; // named lock 용


    public PointCommandService(PointLogRepository pointLogRepository,
                               TotalPointRepository totalPointRepository,
                               JdbcTemplate jdbcTemplate) {
        this.pointLogRepository = pointLogRepository;
        this.totalPointRepository = totalPointRepository;
        this.jdbc = jdbcTemplate;
    }
    // 포인트 적립; 좋아요, 점령, 보너스 + 내역 기록
    // 로그 저장과 포인트 합산은 하나의 논리적 작업. 둘 다 성공해야 커밋
    @Transactional
    public void grant(Long userId, int value, Long sourceId, String sourceType) {
        PointLog.SourceType st = PointLog.SourceType.valueOf(sourceType);

        // 1) 빠른 중복 선조회 (락 없이)
        if (pointLogRepository.existsByUserIdAndSourceTypeAndPointSourceId(userId, st, sourceId)) return;

        String lockKey = buildLockKey(userId, st, sourceId);
        boolean locked = acquireNamedLock(lockKey, 5); // 5초 대기
        if (!locked) {
            // 잠금 못 잡았으면 안전하게 재시도 유도 or 그냥 멱등 간주하고 종료 선택 가능
            // 여기선 '한 번 더 확인 후 종료'로 처리
            if (pointLogRepository.existsByUserIdAndSourceTypeAndPointSourceId(userId, st, sourceId)) return;
            throw new IllegalStateException("잠금 획득 실패");
        }

        try {
            // 2) 잠금 이후 재확인 (double-check)
            if (pointLogRepository.existsByUserIdAndSourceTypeAndPointSourceId(userId, st, sourceId)) return;

            // 3) 처리(레저 먼저 → 잔액)
            pointLogRepository.save(new PointLog(userId, sourceId, st, value));
            totalPointRepository.upsertAdd(userId, value);
        } finally {
            releaseNamedLock(lockKey);
        }
    }

    @Transactional
    public void use(Long userId, int value, Long itemOrOrderId) {
        // STORE: 동일 주문/결제 단위로 멱등 보장하려면 sourceId를 주문ID로 쓰는 것을 권장
        PointLog.SourceType st = PointLog.SourceType.STORE;

        if (pointLogRepository.existsByUserIdAndSourceTypeAndPointSourceId(userId, st, itemOrOrderId)) return;

        String lockKey = buildLockKey(userId, st, itemOrOrderId);
        boolean locked = acquireNamedLock(lockKey, 5);
        if (!locked) {
            if (pointLogRepository.existsByUserIdAndSourceTypeAndPointSourceId(userId, st, itemOrOrderId)) return;
            throw new IllegalStateException("잠금 획득 실패");
        }

        try {
            if (pointLogRepository.existsByUserIdAndSourceTypeAndPointSourceId(userId, st, itemOrOrderId)) return;

            int affected = totalPointRepository.trySubtract(userId, value);
            if (affected == 0) throw new IllegalStateException("포인트 부족");

            // 차감 로그는 음수로 기록
            pointLogRepository.save(new PointLog(userId, itemOrOrderId, PointLog.SourceType.STORE, -value));
        } finally {
            releaseNamedLock(lockKey);
        }
    }

    private String buildLockKey(Long userId, PointLog.SourceType st, Long sourceId) {
        // MySQL named lock key 문자열 (서버/서비스 구분 접두사 붙이면 더 안전)
        return "points:" + st.name() + ":" + userId + ":" + sourceId;
    }

    private boolean acquireNamedLock(String key, int timeoutSeconds) {
        // SELECT GET_LOCK('key', timeout)
        Integer ok = jdbc.queryForObject("SELECT GET_LOCK(?, ?)", Integer.class, key, timeoutSeconds);
        return ok != null && ok == 1;
    }

    private void releaseNamedLock(String key) {
        // DO RELEASE_LOCK('key')
        jdbc.execute((ConnectionCallback<Void>) conn -> {
            try (PreparedStatement ps = conn.prepareStatement("DO RELEASE_LOCK(?)")) {
                ps.setString(1, key);
                ps.execute();
            }
            return null;
        });
    }
}
/*
목적
1) 선조회로 대부분의 재요청을 빠르게 걸러냅니다.
2) named lock으로 같은 비즈니스 키에 대한 동시 요청을 서버 전체에서 직렬화
3) 잠금 구간에서 재확인 → 처리를 하므로 경쟁 상태를 제거합니다.
4) 성공 후에는 기존 upsertAdd/trySubtract가 원자적으로 잔액을 바꿔 줍니다.
 */