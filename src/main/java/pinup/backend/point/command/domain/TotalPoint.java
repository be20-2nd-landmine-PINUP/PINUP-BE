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
    //현재 총 포인트 조회 (읽기 전용)
    public Integer getTotalPoint() { return totalPoint; } // 포인트 잔액 보여주기 위한 조회용 GETTER
    // 포인트를 변경(증가/감소)하는 메서드.
    public void add(int v) { this.totalPoint += v; } // 포인트 변경; 내부 규칙에 따라서만 가능하게
}
/**
 * 사용자별 총 포인트를 관리하는 엔티티 클래스.
 *
 * - 각 사용자는 1개의 TotalPoint 레코드를 가짐.
 * - 누적 포인트(total_point)는 좋아요, 점령, 사용 등의 이벤트에 따라 변경됨.
 * - 단순 합계만 저장하며, 개별 거래 내역은 별도의 로그 테이블에서 관리하는 것이 일반적.
 */