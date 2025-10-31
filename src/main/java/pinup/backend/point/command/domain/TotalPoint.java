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

    protected TotalPoint() {}
    public TotalPoint(Long userId, Integer totalPoint) {
        this.userId = userId;
        this.totalPoint = totalPoint;
    }

    public Long getUserId() { return userId; }
    public Integer getTotalPoint() { return totalPoint; }
    public void add(int v) { this.totalPoint += v; }
}
