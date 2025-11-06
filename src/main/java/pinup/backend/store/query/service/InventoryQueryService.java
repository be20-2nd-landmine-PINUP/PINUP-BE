package pinup.backend.store.query.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.store.command.domain.Inventory;
import pinup.backend.store.command.repository.InventoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryQueryService {

    private final InventoryRepository inventoryRepository;

    // 유저 전체 인벤토리 조회
    public List<Inventory> getUserInventory(Long userId) {
        return inventoryRepository.findAllByUsers_UserId(userId);
    }
}