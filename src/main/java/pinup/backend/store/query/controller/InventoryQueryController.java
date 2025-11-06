package pinup.backend.store.query.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pinup.backend.store.command.dto.InventoryResponseDto;
import pinup.backend.store.query.service.InventoryQueryService;

import java.util.List;

@RestController
@RequestMapping("/inventory/query")
@RequiredArgsConstructor
public class InventoryQueryController {

    private final InventoryQueryService inventoryQueryService;

    // 유저 인벤토리 전체 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<InventoryResponseDto>> getUserInventory(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                inventoryQueryService.getUserInventory(userId)
                        .stream()
                        .map(InventoryResponseDto::fromEntity)
                        .toList()
        );
    }
}
