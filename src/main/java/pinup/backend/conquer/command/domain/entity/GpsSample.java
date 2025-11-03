package pinup.backend.conquer.command.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name="gps_sample")
@Getter @Setter @NoArgsConstructor
public class GpsSample {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="sample_id")
    private Long id;

    @Column(name="session_id", nullable=false)
    private Long sessionId;

    @Column(name="user_id", nullable=false)
    private Integer userId;

    @Column(name="ts", nullable=false)
    private Instant ts;

    @Column(name="accuracy_m")
    private Double accuracyM;

    @Column(name="inside", nullable=false)
    private boolean inside;
}
