package pinup.backend.conquer.command.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name="capture_session")
@Getter @Setter @NoArgsConstructor
public class CaptureSession {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="session_id") private Long id;

    @Column(name="user_id", nullable=false)
    private Integer userId;

    @Column(name="region_id", nullable=false)
    private Integer regionId;   // ← region_id

    @Column(name="started_at", nullable=false)
    private Instant startedAt;

    @Column(name="ended_at")
    private Instant endedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Status status = Status.RUNNING;

    @Column(name="last_poll_at")
    private Instant lastPollAt;

    @Column(name="last_location_at")
    private Instant lastLocationAt;

    public enum Status { RUNNING, CANCELED, COMPLETED }
}
