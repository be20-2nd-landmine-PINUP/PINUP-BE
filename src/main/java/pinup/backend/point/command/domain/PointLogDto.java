package pinup.backend.point.command.domain;

public record PointLogDto(
        String date,        // "YYYY-MM-DD"
        String description, // 사용처
        int amount          // +5 / -10
) {
    public static PointLogDto from(PointLog log) {
        String date = log.getCreatedAt().toLocalDate().toString();
        String description = toDescription(log.getSourceType());
        int amount = log.getPointValue();

        return new PointLogDto(date, description, amount);
    }

    private static String toDescription(PointSourceType type) {
        return switch (type) {
            case CAPTURE -> "지역 정복 보상";
            case LIKE -> "게시글/여행기 좋아요 보상";
            case STORE -> "상점 사용";
            case MONTHLY_BONUS -> "월간 점령 보너스";
        };
    }
}
