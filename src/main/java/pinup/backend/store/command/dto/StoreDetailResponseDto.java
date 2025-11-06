package pinup.backend.store.command.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pinup.backend.store.command.domain.Inventory;
import pinup.backend.store.command.domain.Store;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreDetailResponseDto {

    // 기본 필드
    private Integer itemId;
    private String name;
    private String description;
    private Integer price;
    private String category;
    private String imageUrl;
    private Boolean isActive;
    private String limitType;

    //지역 정보
    private Integer regionCode;
    private String regionName;

    // 인벤토리
    private Boolean isEquipped;
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
                //.regionCode(store.getRegionCode() != null ? store.getRegionCode().getRegionCode() : null)
                //.regionName(store.getRegionCode() != null ? store.getRegionCode().getRegionName() : null)
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
                .regionCode(null)
                .regionName(null)
                .build();
    }
}

