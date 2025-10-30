package pinup.backend.conquer.entity;

import com.hazelcast.internal.metrics.ExcludedMetricTargets;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pinup.backend.member.entity.Users;

import java.util.Date;

@Entity
@Table(name = "territory")
@Getter                                                 // 나중에 controller, service 수정시 수정 예정
@AllArgsConstructor
@NoArgsConstructor
public class Territory {
    // 1. PK ID 선언
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)     // auto-increment
    @Column(name = "territory_id", nullable = false)
    private long territoryId;

    // 2. FK 선언, 단방향
    @ManyToOne(fetch = FetchType.LAZY)                  // ToDo: 일단은 성능 향상을 위해서 LAZY fetch로 선언
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "user_id")
    )
    private Users userId;                               // ToDo: 브랜치 merge 후에 확인 요망

    // 3. 기타 속성 (필드로 선언)
    @Column(name = "capture_start_at")
    private Date captureStartAt;

    @Column(name = "capture_end_at")
    private Date captureEndAt;

    @Column(name = "visit_count")
    private Integer visitCount;

    @Column(name = "photo_url")
    private String photoUrl;
}
