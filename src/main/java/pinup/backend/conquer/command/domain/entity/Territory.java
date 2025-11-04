package pinup.backend.conquer.command.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pinup.backend.member.command.domain.Users;

import java.util.Date;

@Entity
@Table(name = "territory")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Territory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "territory_id", nullable = false)
    private long territoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @Column(name = "capture_start_at")
    private Date captureStartAt;

    @Column(name = "capture_end_at")
    private Date captureEndAt;

    @Column(name = "visit_count")
    private Integer visitCount;

    @Column(name = "photo_url")
    private String photoUrl;
}
