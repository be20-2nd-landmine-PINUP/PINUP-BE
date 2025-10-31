package pinup.backend.point.command.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UsePointRequest(
        @NotNull Long userId,
        @Min(1) int pointValue,
        @NotNull Long sourceId   // store.item_id
) {}
