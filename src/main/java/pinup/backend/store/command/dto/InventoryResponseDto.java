package pinup.backend.store.command.dto;

import lombok.Builder;
import lombok.Getter;
import pinup.backend.store.command.domain.Inventory;
import pinup.backend.store.command.domain.Store;

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

    // 인벤토리 장착 상태
    private Boolean isEquipped;
    private String earnedAt;

    //  Inventory 엔티티를 받아서 화면에 보여줄 데이터 형태로 변환
    public static InventoryResponseDto fromEntity(Inventory inventory) {
        Store store = inventory.getStore();

        return InventoryResponseDto.builder()
                .userId(inventory.getUsers().getUserId())
                .itemId(inventory.getId().getItemId())
                .itemName(store.getName())
                .description(store.getDescription())
                .price(store.getPrice())
                .category(store.getCategory().name())
                .imageUrl(store.getImageUrl())
                .isEquipped(inventory.isEquipped())
                .earnedAt(inventory.getEarnedAt().toString())
                .build();
    }
}
