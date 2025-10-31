package pinup.backend.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.member.command.domain.Users;
import pinup.backend.store.domain.Inventory;
import pinup.backend.store.domain.Store;
import pinup.backend.store.repository.InventoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryService {

   private final InventoryRepository inventoryRepository;

    // 유저의 보유 아이템 조회
    public List<Inventory> getUserInventory(Users user) {
        return inventoryRepository.findAllByUserId(user);
    }

    // 유저의 장착 중인 아이템 목록 조회
    public List<Inventory> getEquippedItems(Users user) {
        return inventoryRepository.findByUserIdAndIsEquippedTrue(user);
    }

    // 이미 보유 중이면 예외 발생
    public void validateOwnedItem(Users user, Store store) {
        boolean alreadyOwned = inventoryRepository.existsByUserIdAndStore(user, store);
        if (alreadyOwned) {
            throw new IllegalStateException("이미 보유 중인 아이템입니다.");
        }
    }

    //인벤토리에 새 아이템 추가
    @Transactional
    public Inventory addToInventory(Users user, Store store) {
        Inventory newItem = Inventory.create(user, store);
        return inventoryRepository.save(newItem);
    }

}
