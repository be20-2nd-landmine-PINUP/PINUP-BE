package pinup.backend.point.command.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GrantPointRequest(
        @NotNull Long userId,
        @Min(1) int pointValue,
        @NotNull Long sourceId,
        @NotBlank String sourceType   // "CAPTURE" | "LIKE" | "STORE"
) {}
