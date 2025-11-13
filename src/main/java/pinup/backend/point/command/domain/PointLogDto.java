package pinup.backend.point.command.domain;

public record PointLogDto(
        String date,
        String description,
        int amount
) {
    public static PointLogDto from(PointLog log) {
        String date = log.getCreatedAt().toLocalDate().toString();

        String description = switch (log.getSourceType()) {
            case CAPTURE -> "지역 정복 보상";
            case LIKE -> "게시글/여행기 좋아요 보상";
            case STORE -> "상점 사용";
            case MONTHLY_BONUS -> "월간 보너스";
        };

        int amount = log.getPointValue();

        return new PointLogDto(date, description, amount);
    }
}
