package pinup.backend.point.command.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "total_point")
public class TotalPoint {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "total_point", nullable = false)
    private Integer totalPoint = 0;

    protected TotalPoint() {} // JPA에서 호출되는 기본 생성자
    public TotalPoint(Long userId, Integer totalPoint) {
        this.userId = userId;
        this.totalPoint = totalPoint;
    } //비즈니스 로직에서 새 객체를 만들 떄 사용 (서비스, 리포지토리)

    public Long getUserId() { return userId; } // 조회용. 외부에서 읽기만 가능함
    public Integer getTotalPoint() { return totalPoint; } // 포인트 잔액 보여주기 위한 조회용 GETTER
    public void add(int v) { this.totalPoint += v; } // 포인트 변경; 내부 규칙에 따라서만 가능하게
}
