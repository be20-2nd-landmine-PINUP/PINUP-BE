package pinup.backend.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pinup.backend.member.command.domain.Users;
import pinup.backend.store.domain.Inventory;
import pinup.backend.store.service.InventoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    //보유 아이템 전체 조회

    // 유저 보유 아이템 조회
    @GetMapping("/inventory")
    public List<Inventory> getUserInventory(@RequestAttribute("user") Users userId) {
        return inventoryService.getUserInventory(userId);
    }
}
