package pinup.backend.point.query.dto;

import java.time.LocalDateTime;
// 포인트 거래 내역 1건 표현.
// 로그 한 건의 상세 데이터
public record PointLogResponse(
        Long logId,
        Long userId,
        Long pointSourceId,
        String sourceType,
        Integer pointValue,
        LocalDateTime createdAt
) {}
