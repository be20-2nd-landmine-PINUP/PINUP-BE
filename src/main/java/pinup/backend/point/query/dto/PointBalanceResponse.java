package pinup.backend.point.query.dto;

// record 문법; DTO를 단순화
// userid, totalpoint 필드 자동생성, 생성자 자동생성, getter자동 생성(userid, totalpoint)
// 불변의 dto 만들기.
/*
public class PointBalanceResponse {
    private final Long userId;
    private final int totalPoint;

    public PointBalanceResponse(Long userId, int totalPoint) {
        this.userId = userId;
        this.totalPoint = totalPoint;
    }

    public Long getUserId() { return userId; }
    public int getTotalPoint() { return totalPoint; }
}
이거 대신함.
 */
public record PointBalanceResponse(Long userId, int totalPoint) {}
// 조회용 dto,
// 한 유저의 현재 총 포인트 잔액