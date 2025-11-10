package pinup.backend.store.command.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pinup.backend.member.command.domain.Users;
import pinup.backend.store.command.domain.Inventory;
import pinup.backend.store.command.dto.InventoryResponseDto;
import pinup.backend.store.command.service.StoreService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
@Tag(name = "Store command", description = "스토어 구매 API")
public class StoreController {

    private final StoreService storeService;

    // 아이템 구매(인벤토리에 자동 추가)
    @PostMapping("/items/{itemId}/purchase")
    @Operation(summary = "스토어 아이템 구매", description = "사용자가 스토어 아이템을 구매하고 인벤토리에 추가합니다.")
    public ResponseEntity<InventoryResponseDto> purchaseItem(
            @Parameter(description = "구매할 아이템 ID", required = true)
            @PathVariable Integer itemId,
            @Parameter(description = "구매를 수행하는 사용자 ID", required = true)
            @RequestParam Long userId
            ) {
        Users userRef = Users.builder()
                .userId(userId)
                .build();

        Inventory inventory = storeService.purchaseItem(userRef, itemId);
        return ResponseEntity.ok(InventoryResponseDto.fromEntity(inventory));
    }
}
