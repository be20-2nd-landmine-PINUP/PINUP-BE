package pinup.backend.store.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.conquer.command.domain.entity.Region;
import pinup.backend.conquer.command.domain.repository.TerritoryRepository;
import pinup.backend.member.command.domain.Users;
import pinup.backend.point.command.domain.PointSourceType;
import pinup.backend.store.command.domain.Inventory;
import pinup.backend.store.command.domain.Store;
import pinup.backend.store.command.domain.StoreLimitType;
import pinup.backend.store.command.repository.StoreRepository;
import pinup.backend.point.command.service.PointService;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreService {

    private final StoreRepository storeRepository;
    private final InventoryService inventoryService;
    private final PointService pointService;
    private final TerritoryRepository territoryRepository;

    /**
     * ✅ 아이템 구매:
     * - 보유여부 확인
     * - 포인트 잔액 확인
     * - 거래기록 전달
     * - 인벤토리 등록
     */
    public Inventory purchaseItem(Users user, Integer itemId) {
        // 1️⃣ 아이템 조회
        Store store = storeRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이템입니다."));

        // 2️⃣ 이미 보유 중인지 확인
        inventoryService.validateOwnedItem(user, store);

        // 지역 한정 아이템 여부 확인
        if (StoreLimitType.LIMITED.equals(store.getLimitType())) {
            validateLimitedItemPurchase(user, store);
        }

        // 3️⃣ 포인트 잔액 확인 (포인트 모듈에서 제공)
        int userPoint = pointService.getUserTotalPoint(user.getUserId());
        int price = store.getPrice();

        if (userPoint < price) {
            throw new IllegalStateException("보유 포인트가 부족합니다. (현재: " + userPoint + " / 필요: " + price + ")");
        }

        // 4️⃣ 거래기록 생성 (포인트 차감은 포인트 도메인 내부에서 수행)
        String eventKey = "STORE_PURCHASE_" + user.getUserId() + "_" + itemId + "_" + System.currentTimeMillis();
        pointService.recordTransaction(
                user,
                PointSourceType.STORE,
                store.getItemId(),
                price,
                eventKey
        );

        // 5️⃣ 인벤토리 등록
        return inventoryService.addToInventory(user, store);
    }

    private void validateLimitedItemPurchase(Users user, Store store) {
        Region region = store.getRegion();

        boolean conquered = territoryRepository.existsByUserIdAndRegion(user, region);
        if (!conquered) {
            throw new IllegalStateException("해당 지역을 점령한 사용자만 구매할 수 있습니다.");
        }
    }

}