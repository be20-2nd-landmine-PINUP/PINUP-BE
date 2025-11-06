package pinup.backend.point.command.service;

import org.springframework.dao.DataIntegrityViolationException;
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
    // ===================== 상수 (정책/키 Prefix) =====================
    private static final int LIKE_POINT = 5;
    private static final int MONTHLY_BONUS_POINT = 10;

    private static final String PREFIX_LIKE = "LIKE";
    private static final String PREFIX_CAPTURE = "CAPTURE";
    private static final String PREFIX_STORE = "STORE";
    private static final String PREFIX_MONTHLY_BONUS_D3 = "MONTHLY_BONUS_D3";

    private static final String LOCK_PREFIX = "points";


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
        String eventKey = likeKey(userId, feedId);
        if (pointLogRepository.existsByEventKey(eventKey)) return;

        String lockKey = lockKey(PREFIX_LIKE, userId, feedId);
        if (!acquireLock(lockKey, 5)) throw new IllegalStateException("잠금 획득 실패");

        try {
            if (pointLogRepository.existsByEventKey(eventKey)) return;

            // 좋아요는 무조건 5점
            PointLog log = PointLog.like(userId, feedId, LIKE_POINT);
            saveIdempotent(log, eventKey); // 유니크 충돌 무시
            totalPointRepository.upsertAdd(userId, LIKE_POINT);
        } finally {
            releaseLock(lockKey);
        }
    }

    /* ============================================
       점령 (region_depth3 기준)
       ============================================ */
    @Transactional
    public void grantCapture(Long userId, Long territoryId) {
        String eventKey = captureKey(userId, territoryId);
        if (pointLogRepository.existsByEventKey(eventKey)) return;

        String lockKey = lockKey(PREFIX_CAPTURE, userId, territoryId);
        if (!acquireLock(lockKey, 5)) throw new IllegalStateException("잠금 획득 실패");

        try {
            if (pointLogRepository.existsByEventKey(eventKey)) return;
            // 영토의 행정구역 이름(region_depth3)을 db에서 가져옴
            String depth3 = jdbc.queryForObject("""
                SELECT r.region_depth3
                FROM territory t
                JOIN region r ON r.region_id = t.region_id
                WHERE t.territory_id = ?
                """, String.class, territoryId
            );
            int value = calcCapturePoint(depth3); // 점수 계산

            PointLog log = PointLog.capture(userId, territoryId, value);
            saveIdempotent(log, eventKey);
            if (value != 0) {
                totalPointRepository.upsertAdd(userId, value);
            }
        } finally {
            releaseLock(lockKey);
        }
    }


    /* ============================================
       포인트 차감 (STORE)
       ============================================ */
    @Transactional
    public void use(Long userId, int value, Long sourceId) {
        String eventkey = storeKey(userId, sourceId);
        if (pointLogRepository.existsByEventKey(eventkey)) return;

        String lockKey = lockKey(PREFIX_STORE, userId, sourceId);
        if (!acquireLock(lockKey, 5)) throw new IllegalStateException("잠금 획득 실패");

        try {
            if (pointLogRepository.existsByEventKey(eventkey)) return;

            int affected = totalPointRepository.trySubtract(userId, value);
            if (affected == 0)
                throw new IllegalStateException("포인트 부족");

            PointLog log = PointLog.storeUse(userId, sourceId, -value);
            saveIdempotent(log, eventkey);
        } finally {
            releaseLock(lockKey);
        }
    }

    /* =========================================================
      월 보너스 (MONTHLY_BONUS)
      - yearMonth = YYYYMM (예: 202510)
      - event_key: event_key = "MONTHLY_BONUS:{userId}:{territoryId}:{YYYYMM}"
      - point_source_id = (d1|d2|d3).hashCode() & 0x7fffffff (INT 안전 범위)
      - 과거의 음수 point_source_id/월 범위 조회 방식은 폐기
      ========================================================= */
    @Transactional
    public void grantMonthlyBonusByDepth3(Long userId, String d1, String d2, String d3, int yearMonth) {
        String sig = normalizeDepths(d1, d2, d3); // "d1|d2|d3"
        String eventKey = monthlyBonusD3Key(userId, sig, yearMonth);
        if (pointLogRepository.existsByEventKey(eventKey)) return;

        String lockKey = lockKey(PREFIX_MONTHLY_BONUS_D3, userId, sig, yearMonth);
        if (!acquireLock(lockKey, 5)) throw new IllegalStateException("잠금 획득 실패");
        try {
            if (pointLogRepository.existsByEventKey(eventKey)) return;

            int sourceId = positiveHash(sig);
            PointLog log = new PointLog(
                    userId,
                    (long) sourceId,                          // INT 범위 내 식별자
                    PointLog.SourceType.MONTHLY_BONUS,
                    eventKey,
                    MONTHLY_BONUS_POINT
            );
            saveIdempotent(log, eventKey);
            totalPointRepository.upsertAdd(userId, MONTHLY_BONUS_POINT);
        } finally {
            releaseLock(lockKey);
        }
    }



    /* ===================== 유틸/헬퍼 ===================== */

    // 유니크(event_key) 충돌 시 멱등 처리로 간주하고 무시
    private void saveIdempotent(PointLog log, String eventKey) {
        try {
            pointLogRepository.save(log);
        } catch (DataIntegrityViolationException e) {
            // 정말 같은 키로 이미 있는지 확인 (다른 제약 위반이면 다시 던짐)
            if (!pointLogRepository.existsByEventKey(eventKey)) throw e;
        }
    }

    // 행정 구역에 따른 점수 계산 로직
    private int calcCapturePoint(String d3) {
        if (d3 == null) return 0;
        String s = d3.trim();
        if (s.endsWith("읍") || s.endsWith("면")) return 10;
        if (s.endsWith("동")) return 5;
        return 0;
    }
    // 월 경계가 필요한 다른 로직에서 쓸 수 있도록 도우미(지금은 미사용)
    @SuppressWarnings("unused")
    private LocalDateTime[] monthRangeKst(int yearMonth) {
        ZoneId zone = ZoneId.of("Asia/Seoul");
        int y = yearMonth / 100;
        int m = yearMonth % 100;
        LocalDateTime from = LocalDate.of(y, m, 1).atStartOfDay(zone).toLocalDateTime();
        LocalDateTime to = from.plusMonths(1);
        return new LocalDateTime[]{from, to};
    }

    // --------------------- MySQL 분산락 ---------------------

    // 분산락(GET_LOCK/RELEASE_LOCK)
    private boolean acquireLock(String key, int timeoutSec) {
        Integer r = jdbc.queryForObject("SELECT GET_LOCK(?, ?)", Integer.class, key, timeoutSec);
        return r != null && r == 1;
    }

    private void releaseLock(String key) {
        jdbc.queryForObject("SELECT RELEASE_LOCK(?)", Integer.class, key);
    }
    // --------------------- 키 생성 (상수 기반) ---------------------

    // event_key 생성 규칙(일관성)
    private String likeKey(Long userId, Long feedId) {
        return "LIKE:%d:%d".formatted(PREFIX_LIKE, userId, feedId);
    }

    private String captureKey(Long userId, Long territoryId) {
        return "CAPTURE:%d:%d".formatted(PREFIX_LIKE, userId, territoryId);
    }

    private String storeKey(Long userId, Long sourceId) {
        return "STORE:%d:%d".formatted(PREFIX_LIKE, userId, sourceId);
    }

    private String monthlyBonusD3Key(Long userId, String sig, int yearMonth) {
        return "%s:%d:%s:%d".formatted(PREFIX_MONTHLY_BONUS_D3, userId, sig, yearMonth);
    }

    private String lockKey(String prefix, Long id1, Long id2) {
        return "%s:%s:%d:%d".formatted(LOCK_PREFIX, prefix, id1, id2);
    }

    private String lockKey(String prefix, Long id1, String sig, int yearMonth) {
        return "%s:%s:%d:%s:%d".formatted(LOCK_PREFIX, prefix, id1, sig, yearMonth);
    }

    private String normalizeDepths(String d1, String d2, String d3) {
        String n1 = d1 == null ? "" : d1.trim();
        String n2 = d2 == null ? "" : d2.trim();
        String n3 = d3 == null ? "" : d3.trim();
        return n1 + "|" + n2 + "|" + n3;
    }

    private int positiveHash(String s) {
        return (s == null) ? 0 : (s.hashCode() & 0x7fffffff);
    }




}


/*
목적
1) 선조회로 대부분의 재요청을 빠르게 걸러냅니다.
2) named lock으로 같은 비즈니스 키에 대한 동시 요청을 서버 전체에서 직렬화
3) 잠금 구간에서 재확인 → 처리를 하므로 경쟁 상태를 제거합니다.
4) 성공 후에는 기존 upsertAdd/trySubtract가 원자적으로 잔액을 바꿔 줍니다.
 */