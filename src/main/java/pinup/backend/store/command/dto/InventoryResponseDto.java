package pinup.backend.store.command.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import pinup.backend.store.command.domain.Inventory;
import pinup.backend.store.command.domain.Store;

@Getter
@Builder
public class InventoryResponseDto {

    // 복합키 정보
    @Schema(description = "사용자 ID", example = "7")
    private Long userId;
    @Schema(description = "아이템 ID", example = "15")
    private Integer itemId;

    // 아이템 정보
    @Schema(description = "아이템 이름", example = "서울 특별 마커")
    private String itemName;
    @Schema(description = "아이템 설명")
    private String description;
    @Schema(description = "아이템 가격", example = "5000")
    private Integer price;
    @Schema(description = "아이템 카테고리", example = "MARKER")
    private String category;
    @Schema(description = "아이템 이미지 URL")
    private String imageUrl;

    // 인벤토리 장착 상태
    @Schema(description = "아이템 장착여부", example = "false")
    private Boolean isEquipped;
    @Schema(description = "획득 일시", example = "2024-12-01T15:00:00")
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
