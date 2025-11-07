package pinup.backend.point.command.domain;

import jakarta.persistence.*;
import lombok.*;
import pinup.backend.member.command.domain.Users;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "total_point")
public class TotalPoint {

    /**
     * ✅ user_id : Users 엔티티의 PK를 그대로 사용 (1:1 매핑)
     */
    @Id
    @Column(name = "user_id")
    private Long userId;

    /**
     * ✅ 누적 포인트 (기본값 0)
     */
    @Column(name = "total_point", nullable = false)
    private int totalPoint;

    /**
     * ✅ Users 엔티티와 1:1 관계 (읽기 전용, 외래키 연결)
     * mappedBy 없이 JoinColumn으로 직접 지정
     */
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private Users user;

    // 💡 누적 포인트 증가/차감 로직
    public void addPoints(int value) {
        this.totalPoint += value;
    }

    public void subtractPoints(int value) {
        this.totalPoint -= value;
        if (this.totalPoint < 0) this.totalPoint = 0;
    }
}
