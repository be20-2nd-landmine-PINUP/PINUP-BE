package pinup.backend.store.command.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pinup.backend.conquer.command.domain.entity.Region;
import pinup.backend.store.command.domain.Inventory;
import pinup.backend.store.command.domain.Store;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreDetailResponseDto {

    // 기본 필드
    @Schema(description = "아이템 ID", example = "1")
    private Integer itemId;
    @Schema(description = "아이템 이름", example = "서울 특별 마커")
    private String name;
    @Schema(description = "아이템 설명", example = "서울 지역 한정판 마커")
    private String description;
    @Schema(description = "아이템 가격", example = "5000")
    private Integer price;
    @Schema(description = "아이템 카테고리", example = "MARKER")
    private String category;
    @Schema(description = "아이템 이미지 URL")
    private String imageUrl;
    @Schema(description = "판매 중 여부", example = "true")
    private Boolean isActive;
    @Schema(description = "판매 정책", example = "LIMITED")
    private String limitType;

    //지역 정보
    @Schema(description = "행정구역 ID", example = "101")
    private Long regionId;
    @Schema(description = "행정구역 이름", example = "서울특별시 강남구")
    private String regionName;

    // 인벤토리
    @Schema(description = "아이템 장착 여부", example = "false")
    private Boolean isEquipped;
    @Schema(description = "아이템 보유 여부", example = "true")
    private Boolean isOwned;


    // (목록 / 상세조회)
    public static StoreDetailResponseDto fromEntity(Store store) {
        return StoreDetailResponseDto.builder()
                .itemId(store.getItemId())
                .name(store.getName())
                .description(store.getDescription())
                .price(store.getPrice())
                .category(store.getCategory().name())
                .imageUrl(store.getImageUrl())
                .isActive(store.isActive())
                .limitType(store.getLimitType().name())
                .regionId((Long) resolveRegionId(store.getRegion()))
                .regionName(resolveRegionName(store.getRegion()))
                .isEquipped(null) // 조회 시엔 null
                .isOwned(null)
                .build();
    }


    // 구매 응답 시
    public static StoreDetailResponseDto fromPurchase(Store store, Inventory inventory) {
        return StoreDetailResponseDto.builder()
                .itemId(store.getItemId())
                .name(store.getName())
                .category(store.getCategory().name())
                .imageUrl(store.getImageUrl())        // 구매 후 미리보기용 이미지
                .isEquipped(inventory.isEquipped())
                .isOwned(true)
                .description(null)
                .price(null)
                .isActive(null)
                .limitType(store.getLimitType().name())
                .regionId((Long) resolveRegionId(store.getRegion()))
                .regionName(resolveRegionName(store.getRegion()))
                .build();
    }

    private static Object resolveRegionId(Region region) {
        return region != null ? region.getRegionId() : null;
    }

    private static String resolveRegionName(Region region) {
        return region != null ? region.getRegionName() : null;
    }
}

