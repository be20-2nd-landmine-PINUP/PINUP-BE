package pinup.backend.conquer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "te")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TerritoryVisitLog {

    // 1. PK ID 선언
    @Id
    @Column(name = "visit_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    // 2. FK 선언 (양방향 x)
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("territory_id")
    @JoinColumn(
            name = "territory_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "territory_id")

    )
    private Territory territoryId;

    // 3. 기타 속성 (필드로 선언)
    @Column(name = "duration_minutes")
    private int durationMinutes;   // ToDO: 나중에 시간 입력 방식 고민 후 수정 예정

    @Column(name = "is_valid", nullable = false)
    private boolean isValid;

    @Column(name = "visited_at", nullable = false)
    private Date visitedAt;




}
