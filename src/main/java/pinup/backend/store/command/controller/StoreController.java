package pinup.backend.store.command.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pinup.backend.member.command.domain.Users;
import pinup.backend.store.command.domain.Inventory;
import pinup.backend.store.command.domain.Store;
import pinup.backend.store.command.dto.StoreDetailResponseDto;
import pinup.backend.store.command.service.InventoryService;
import pinup.backend.store.command.service.StoreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;
    private final InventoryService inventoryService;


    // 전체 판매중 아이템 조회(일반/한정판 포함)
    @GetMapping("/items")
    public List<StoreDetailResponseDto> getActiveItems() {
        List<Store> items = storeService.getActiveItems();
        return items.stream()
                .map(StoreDetailResponseDto::fromEntity)
                .toList();
    }

    // 단일 아이템 상세 조회(일반/한정판 포함)
    @GetMapping("/items/{itemId}")
    public StoreDetailResponseDto getItemDetail(@PathVariable Integer itemId) {
        Store item = storeService.getItemById(itemId);
        return StoreDetailResponseDto.fromEntity(item);
    }

    // 아이템 구매(인벤토리에 자동 추가)
    @PostMapping("/purchase/{itemId}")
    public Inventory purchaseItem(
            @PathVariable Integer itemId,
            @RequestAttribute("user")Users usersId
            ) {
            return storeService.purchaseItem(usersId, itemId);
    }
}
