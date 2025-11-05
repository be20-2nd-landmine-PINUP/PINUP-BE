package pinup.backend.point.command.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.point.command.domain.PointLog;
import pinup.backend.point.command.repository.PointLogRepository;
import pinup.backend.point.command.repository.TotalPointRepository;

import java.time.LocalDateTime;

import java.time.*; // LocalDate, LocalDateTime, ZoneId 등



// 포인트 적립, 차감하는 기능; 쓰기 전용
// 중복 체크 -> named lock ->재확인 -> 처리

@Service
public class PointCommandService {

    private final PointLogRepository pointLogRepository;
    private final TotalPointRepository totalPointRepository;
    private final JdbcTemplate jdbc;

    public PointCommandService(PointLogRepository pointLogRepository,
                               TotalPointRepository totalPointRepository,
                               JdbcTemplate jdbc) {
        this.pointLogRepository = pointLogRepository;
        this.totalPointRepository = totalPointRepository;
        this.jdbc = jdbc;
    }

    /* ============================================
       좋아요 (+5)
       ============================================ */
    @Transactional
    public void grantLike(Long userId, Long feedId) {
        if (exists(userId, PointLog.SourceType.LIKE, feedId)) return;  // 이미 있으면 중복 방지

        String lockKey = buildLockKey(userId, PointLog.SourceType.LIKE, feedId);
        if (!acquireLock(lockKey, 5)) throw new IllegalStateException("잠금 획득 실패");

        try {
            if (exists(userId, PointLog.SourceType.LIKE, feedId)) return;

            int value = 5; // 좋아요는 무조건 5점
            pointLogRepository.save(new PointLog(userId, feedId, PointLog.SourceType.LIKE, value));
            totalPointRepository.upsertAdd(userId, value);
        } finally {
            releaseLock(lockKey);
        }
    }

    /* ============================================
       점령 (region_depth3 기준)
       ============================================ */
    @Transactional
    public void grantCapture(Long userId, Long territoryId) {
        if (exists(userId, PointLog.SourceType.CAPTURE, territoryId)) return; // 멱등

        String lockKey = buildLockKey(userId, PointLog.SourceType.CAPTURE, territoryId);
        if (!acquireLock(lockKey, 5)) throw new IllegalStateException("잠금 획득 실패");

        try {
            if (exists(userId, PointLog.SourceType.CAPTURE, territoryId)) return;
            // 영토의 행정구역 이름(region_depth3)을 db에서 가져옴
            String depth3 = jdbc.queryForObject(
                    "SELECT region_depth3 FROM territory WHERE territory_id = ?",
                    String.class, territoryId
            );
            int value = calcCapturePoint(depth3); // 점수 계산

            pointLogRepository.save(new PointLog(userId, territoryId, PointLog.SourceType.CAPTURE, value));
            totalPointRepository.upsertAdd(userId, value);
        } finally {
            releaseLock(lockKey);
        }
    }
    // 행정 구역에 따른 점수 계산 로직
    private int calcCapturePoint(String d3) {
        if (d3 == null) return 0;
        d3 = d3.trim();
        if (d3.endsWith("읍") || d3.endsWith("면")) return 10; // 읍, 면이면 +10점
        if (d3.endsWith("동")) return 5; // 동이면 5점
        return 0; // 그 외는 점수 없음
    }

    /* ============================================
       포인트 차감 (STORE)
       ============================================ */
    @Transactional
    public void use(Long userId, int value, Long sourceId) {
        if (exists(userId, PointLog.SourceType.STORE, sourceId)) return; // 멱등

        String lockKey = buildLockKey(userId, PointLog.SourceType.STORE, sourceId);
        if (!acquireLock(lockKey, 5)) throw new IllegalStateException("잠금 획득 실패");

        try {
            if (exists(userId, PointLog.SourceType.STORE, sourceId)) return;

            int affected = totalPointRepository.trySubtract(userId, value);
            if (affected == 0)
                throw new IllegalStateException("포인트 부족");

            pointLogRepository.save(new PointLog(userId, sourceId, PointLog.SourceType.STORE, -value));
        } finally {
            releaseLock(lockKey);
        }
    }

    // 보너스 전용 메서드
    //monthlyKey는 YYYYMM(예: 202510) 그대로 point_log.point_source_id에 들어갑니다.
    @Transactional
    public void grantMonthlyBonus(Long userId, Long territoryId, long monthlyKey) {
        // 월 구간 계산 (Asia/Seoul)
        ZoneId zone = ZoneId.of("Asia/Seoul");
        int year = (int) (monthlyKey / 100);
        int month = (int) (monthlyKey % 100);
        LocalDateTime from = LocalDate.of(year, month, 1).atStartOfDay(zone).toLocalDateTime();
        LocalDateTime to   = LocalDate.of(year, month, 1).plusMonths(1).atStartOfDay(zone).toLocalDateTime();

        // 보너스 구분용: point_source_id = -territoryId  (INT DDL과 충돌 없음)
        int bonusSourceId = Math.toIntExact(-territoryId);

        // 멱등: 같은 달에 같은 영토 보너스가 이미 있으면 스킵
        if (existsBonusThisMonth(userId, bonusSourceId, from, to)) return;

        String lockKey = buildLockKey("BONUS", userId, territoryId, monthlyKey);
        if (!acquireLock(lockKey, 5)) throw new IllegalStateException("잠금 획득 실패");

        try {
            if (existsBonusThisMonth(userId, bonusSourceId, from, to)) return;

            int value = 10; // 보너스 고정 +10
            pointLogRepository.save(new PointLog(
                    userId,
                    (long) bonusSourceId,               // 음수 territoryId
                    PointLog.SourceType.CAPTURE,       // DDL 제약상 CAPTURE로 저장
                    value
            ));
            totalPointRepository.upsertAdd(userId, value);
        } finally {
            releaseLock(lockKey);
        }
    }
    private boolean existsBonusThisMonth(Long userId, int bonusSourceId,
                                         LocalDateTime from, LocalDateTime to) {
        Integer cnt = jdbc.queryForObject("""
        SELECT COUNT(*) FROM point_log
         WHERE user_id = ?
           AND source_type = 'CAPTURE'
           AND point_source_id = ?
           AND created_at >= ?
           AND created_at <  ?
    """, Integer.class, userId, bonusSourceId, from, to);
        return cnt != null && cnt > 0;
    }


    private String buildLockKey(String kind, Long userId, Long territoryId, long monthlyKey) {
        return "points:%s:%s:%s:%s".formatted(kind, userId, territoryId, monthlyKey);
    }

    private boolean acquireLock(String key, int timeoutSec) {
        Integer r = jdbc.queryForObject("SELECT GET_LOCK(?, ?)", Integer.class, key, timeoutSec);
        return r != null && r == 1;
    }

    private void releaseLock(String key) {
        jdbc.queryForObject("SELECT RELEASE_LOCK(?)", Integer.class, key);
    }

    // 추가 ⬇️
    private String buildLockKey(Long userId, PointLog.SourceType type, Long sourceId) {
        return "points:%s:%s:%s".formatted(type.name(), userId, sourceId);
    }
    private boolean exists(Long userId, PointLog.SourceType type, Long sourceId) {
        return pointLogRepository.existsByUserIdAndSourceTypeAndPointSourceId(userId, type, sourceId);
    }


}


/*
목적
1) 선조회로 대부분의 재요청을 빠르게 걸러냅니다.
2) named lock으로 같은 비즈니스 키에 대한 동시 요청을 서버 전체에서 직렬화
3) 잠금 구간에서 재확인 → 처리를 하므로 경쟁 상태를 제거합니다.
4) 성공 후에는 기존 upsertAdd/trySubtract가 원자적으로 잔액을 바꿔 줍니다.
 */