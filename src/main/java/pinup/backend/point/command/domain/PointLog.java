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
    private SourceType sourceType; // CAPTURE, LIKE, STORE

    @Column(name = "point_value", nullable = false)
    private Integer pointValue;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public enum SourceType { CAPTURE, LIKE, STORE }

    protected PointLog() {}
    public PointLog(Long userId, Long pointSourceId, SourceType sourceType, Integer pointValue) {
        this.userId = userId;
        this.pointSourceId = pointSourceId;
        this.sourceType = sourceType;
        this.pointValue = pointValue;
    }
}
