package pinup.backend.store.command.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pinup.backend.store.command.domain.StoreItemCategory;
import pinup.backend.store.command.domain.StoreLimitType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreRequestDto {

    @Schema(description = "요청을 수행하는 관리자 ID", example = "1")
    private Long adminId;
    @Schema(description = "아이템이 속한 행정구역 ID (일반 아이템의 경우 null 허용)", example = "101")
    private Long regionId;
    @Schema(description = "아이템 이름", example = "서울 특별 마커")
    private String name;
    @Schema(description = "아이템 설명", example = "서울 지역 한정판 마커")
    private String description;
    @Schema(description = "아이템 가격", example = "5000")
    private int price;
    @Schema(description = "아이템 카테고리", example = "MARKER")
    private StoreItemCategory category;
    @Schema(description = "판매 정책", example = "LIMITED")
    private StoreLimitType limitType;
    @Schema(description = "아이템 이미지 URL", example = "https://cdn.pinup.com/store/seoul.png")
    private String imageUrl;

}
