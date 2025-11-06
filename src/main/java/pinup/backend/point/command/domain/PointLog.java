package pinup.backend.point.command.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "point_log")
public class PointLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "point_source_id", nullable = false)
    private Long pointSourceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false)
    private SourceType sourceType; // CAPTURE, LIKE, STORE, MONTHLY_BONUS

    @Column(name = "event_key", nullable = false, unique = true)
    private String eventKey; // DDL에 맞춰 추가

    @Column(name = "point_value", nullable = false)
    private Integer pointValue;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public enum SourceType { CAPTURE, LIKE, STORE, MONTHLY_BONUS }
/// STORE로 변경한 것 잘 반영된 것 맞는지?
    protected PointLog() {}

    public PointLog(Long userId, Long pointSourceId, SourceType sourceType, String eventKey, Integer pointValue) {
        this.userId = userId;
        this.pointSourceId = pointSourceId;
        this.sourceType = sourceType;
        this.eventKey = eventKey;
        this.pointValue = pointValue;
    }
    // -------------------------------
    // ✅ 정적 팩토리 메서드
    // -------------------------------

    /** 좋아요 적립 (+5) */
    public static PointLog like(Long userId, Long feedId, int value) {
        String key = "LIKE:%d:%d".formatted(userId, feedId);
        return new PointLog(userId, feedId, SourceType.LIKE, key, value);
    }

    /** 점령 적립 (읍/면/동 단위, +5 또는 +10) */
    public static PointLog capture(Long userId, Long territoryId, int value) {
        String key = "CAPTURE:%d:%d".formatted(userId, territoryId);
        return new PointLog(userId, territoryId, SourceType.CAPTURE, key, value);
    }

    /** 포인트 사용 (STORE, 음수 값 저장) */
    public static PointLog storeUse(Long userId, Long orderId, int minusValue) {
        String key = "STORE:%d:%d".formatted(userId, orderId);
        return new PointLog(userId, orderId, SourceType.STORE, key, minusValue);
    }

    /** 월별 보너스 (+10, 같은 달/영토당 1회) */
    public static PointLog monthlyBonus(Long userId, Long territoryId, int yearMonth, int value) {
        String key = "MONTHLY_BONUS:%d:%d:%d".formatted(userId, territoryId, yearMonth);
        return new PointLog(userId, territoryId, SourceType.MONTHLY_BONUS, key, value);
    }


}


