package pinup.backend.store.dto;

import lombok.Builder;
import lombok.Getter;
import pinup.backend.store.domain.Inventory;
import pinup.backend.store.domain.Store;

@Getter
@Builder
public class StoreItemResponseDto {

    // 기본 필드
    private Integer itemId;
    private String name;
    private String description;
    private Integer price;
    private String category;
    private String imageUrl;
    private Boolean isActive;

    // 지역 정보
    // private Integer regionCode;
    //private String regionName;

    // 인벤토리
    private Boolean isEquipped;
    private Boolean isOwned;


    // (목록 / 상세조회)
    public static StoreItemResponseDto fromEntity(Store store) {
        return StoreItemResponseDto.builder()
                .itemId(store.getItemId())
                .name(store.getName())
                .description(store.getDescription())
                .price(store.getPrice())
                .category(store.getCategory().name())
                .imageUrl(store.getImageUrl())
                .isActive(store.isActive())
                //.regionCode(store.getRegionCode() != null ? store.getRegionCode().getRegionCode() : null)
                //.regionName(store.getRegionCode() != null ? store.getRegionCode().getRegionName() : null)
                .isEquipped(null) // 조회 시엔 null
                .isOwned(null)
                .build();
    }

    // 구매 응답 시
    public static StoreItemResponseDto fromPurchase(Store store, Inventory inventory) {
        return StoreItemResponseDto.builder()
                .itemId(store.getItemId())
                .name(store.getName())
                .description(store.getDescription())
                .price(store.getPrice())
                .category(store.getCategory().name())
                .imageUrl(store.getImageUrl())
                .isActive(store.isActive())
                //.regionCode(store.getRegionCode() != null ? store.getRegionCode().getRegionCode() : null)
                //.regionName(store.getRegionCode() != null ? store.getRegionCode().getRegionName() : null)
                .isEquipped(inventory.isEquipped())
                .isOwned(true)
                .build();
    }
}

