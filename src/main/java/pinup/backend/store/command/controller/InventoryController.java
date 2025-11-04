package pinup.backend.store.command.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pinup.backend.member.command.domain.Users;
import pinup.backend.store.command.domain.Inventory;
import pinup.backend.store.command.dto.InventoryResponseDto;
import pinup.backend.store.command.service.InventoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    // 유저 보유 아이템 조회
    @GetMapping
    public List<InventoryResponseDto> getUserInventory(@RequestAttribute("user") Users users) {
        return inventoryService.getUserInventory(users)
                .stream()
                .map(InventoryResponseDto::fromEntity)
                .toList();
    }
}
