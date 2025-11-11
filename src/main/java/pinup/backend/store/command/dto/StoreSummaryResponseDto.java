package pinup.backend.store.command.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreSummaryResponseDto {

    @Schema(description = "아이템 ID", example = "1")
    private Integer itemId;
    @Schema(description = "아이템 이름", example = "서울 특별 마커")
    private String name;
    @Schema(description = "아이템 가격", example = "5000")
    private Integer price;
    @Schema(description = "아이템 카테고리", example = "MARKER")
    private String category;
    @Schema(description = "아이템 이미지 URL")
    private String imageUrl;
    @Schema(description = "판매 정책", example = "LIMITED")
    private String isLimited;
    @Schema(description = "판매 중 여부", example = "true")
    private Boolean isActive;
}
