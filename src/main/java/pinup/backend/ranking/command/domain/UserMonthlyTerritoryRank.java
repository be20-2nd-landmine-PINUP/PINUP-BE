package pinup.backend.ranking.command.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_monthly_territory_rank",
        indexes = {
                @Index(name = "idx_rank", columnList = "year_month, rank_int")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMonthlyTerritoryRank {

    @EmbeddedId
    private UserMonthlyTerritoryRankKey id;

    @Column(name = "completed_count", nullable = false)
    private int completedCount;

    @Column(name = "rank_int", nullable = false)
    private int rankInt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
