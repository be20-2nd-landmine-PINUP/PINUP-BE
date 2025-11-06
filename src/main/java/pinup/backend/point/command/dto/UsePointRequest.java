package pinup.backend.point.command.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
// 차감
public record UsePointRequest(
        @NotNull Long userId,
        @Min(1) int pointValue,
        @NotNull Long sourceId   // 주문/거래 아이디
) {}
