package pinup.backend.store.dto;

import lombok.Builder;
import lombok.Getter;
import pinup.backend.store.domain.Inventory;
import pinup.backend.store.domain.Store;

@Getter
@Builder
public class InventoryResponseDto {

    // 복합키 정보
    private Long userId;
    private Integer itemId;

    // 아이템 정보
    private String itemName;
    private String description;
    private Integer price;
    private String category;
    private String imageUrl;

    // 인벤토리 상태
    private Boolean isEquipped;
    private String earnedAt;

    public static InventoryResponseDto freomEntity(Inventory inventory) {
        Store store = inventory.getStore();

        return InventoryResponseDto.builder()
                .userId(inventory.getId().getUserId())
                .itemId(store.getItemId())
                .itemName(store.getName())
                .description(store.getDescription())
                .price(store.getPrice())
                .category(store.getCategory().name())  // Enum → String 변환
                .imageUrl(store.getImageUrl())
                .isEquipped(inventory.isEquipped())
                .earnedAt(inventory.getEarnedAt().toString())  // LocalDateTime → String
                .build();
    }
}
