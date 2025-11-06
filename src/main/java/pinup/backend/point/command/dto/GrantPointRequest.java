package pinup.backend.point.command.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// 적립
// CONTROLLER로 들어오는 JSON 데이터를 자바 객체로 바꾸는 것.
public record GrantPointRequest(
        @NotNull Long userId,
        @NotNull Long sourceId
) {}
