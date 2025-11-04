package pinup.backend.ranking.command.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
// 사용자 월간 랭킹 정보를 저장하는 JPA 엔티티 클래스
@Entity
@Table(
        name = "user_monthly_territory_rank", // 실제 DB테이블 이름 지정
        indexes = {
                // year_month + rank_int 조합에 인덱스를 생성 (조회 성능 향상)
                @Index(name = "idx_rank", columnList = "year_month, rank_int")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMonthlyTerritoryRank {
    // 복합 기본키(연월 + 사용자id)를 포함한 임베디드 아이디
    // 즉, 기본키(id)대신 복합키(embeddedid)사용; serMonthlyTerritoryRankKey 클래스가 기본키 역할을 합니다.
    @EmbeddedId
    private UserMonthlyTerritoryRankKey id;
    // 사용자가 해당 월에 완료한 횟수
    @Column(name = "completed_count", nullable = false)
    private int completedCount;
    // 월간 랭킹 순위(숫자)
    @Column(name = "rank_int", nullable = false)
    private int rankInt;
    // 레코드 생성시 자동으로 입력되는 타임스탬프
    // Hibernate의 @CreationTimestamp 사용 → insert 시 자동 채워짐
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
