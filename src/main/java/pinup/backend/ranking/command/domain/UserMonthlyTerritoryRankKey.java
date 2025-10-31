package pinup.backend.ranking.command.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMonthlyTerritoryRankKey implements Serializable {

    @Column(name = "year_month", length = 7, nullable = false)
    private String yearMonth; // 예: "2025-10"

    @Column(name = "user_id", nullable = false)
    private Long userId;
}
