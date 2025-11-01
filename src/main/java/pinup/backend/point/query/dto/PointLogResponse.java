package pinup.backend.point.query.dto;

import java.time.LocalDateTime;

public record PointLogResponse(
        Long logId,
        Long userId,
        Long pointSourceId,
        String sourceType,
        Integer pointValue,
        LocalDateTime createdAt
) {}
