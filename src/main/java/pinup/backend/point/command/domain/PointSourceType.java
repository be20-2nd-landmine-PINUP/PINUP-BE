package pinup.backend.point.command.domain;

public enum PointSourceType {
    /**
     * CAPTURE : 지역 점령 시 포인트 지급
     * LIKE : 게시물/여행기 좋아요 시 포인트 지급
     * STORE : 상점에서 아이템 구매 시 포인트 사용
     * MONTHLY_BONUS : 월간 보너스 포인트 지급
     */
    CAPTURE,
    LIKE,
    STORE,
    MONTHLY_BONUS
}