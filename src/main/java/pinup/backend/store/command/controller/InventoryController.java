package pinup.backend.store.command.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pinup.backend.store.command.service.InventoryService;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    // 인벤토리에 아이템 추가
    @PostMapping("/{userId}/add/{itemId}")
    public ResponseEntity<?> addItem(
            @PathVariable Long userId,
            @PathVariable Integer itemId
    ) {
        return ResponseEntity.ok(Map.of(
                "message", "userId=" + userId + " 의 인벤토리에 itemId=" + itemId + " 추가됨"
        ));
    }
}