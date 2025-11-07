package pinup.backend.store.command.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pinup.backend.member.command.domain.Users;
import pinup.backend.store.command.domain.Inventory;
import pinup.backend.store.command.dto.InventoryResponseDto;
import pinup.backend.store.command.service.StoreService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store/command")
public class StoreController {

    private final StoreService storeService;

    // 아이템 구매(인벤토리에 자동 추가)
    @PostMapping("/purchase/{itemId}")
    public ResponseEntity<InventoryResponseDto> purchaseItem(
            @PathVariable Integer itemId,
            @PathVariable Long userId
            ) {
        Users userRef = Users.builder()
                .userId(userId)
                .build();

        Inventory inventory = storeService.purchaseItem(userRef, itemId);
        return ResponseEntity.ok(InventoryResponseDto.fromEntity(inventory));
    }
}
