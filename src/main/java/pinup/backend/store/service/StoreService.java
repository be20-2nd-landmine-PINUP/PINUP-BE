package pinup.backend.store.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.member.command.domain.Users;
import pinup.backend.store.domain.Inventory;
import pinup.backend.store.domain.Store;
import pinup.backend.store.domain.StoreItemCategory;
import pinup.backend.store.domain.StoreLimitType;
import pinup.backend.store.repository.StoreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreService {

    private final StoreRepository storeRepository;
    private final InventoryService inventoryService;
    //private final ConquerRepository conquerRepository;
    //private final PointService pointService;

    // 판매 중인 아이템 조회(지역코드로 한정판 구분 가능)
    public List<Store> getActiveItems() {
        return storeRepository.findAllByIsActiveTrue();
    }

    // 단일 아이템 상세정보 조회
    public Store getItemById(Integer itemId) {
        return storeRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException
                        ("해당 아이템이 존재하지 않습니다. ID: " + itemId));
    }

    // 아이템 구매(포인트 모듈 호출)
    public Inventory purchaseItem(Users user, Integer itemId) {
        // 아이템 조회
        Store store = getItemById(itemId);

        // 아이템 보유 중 검증
        inventoryService.validateOwnedItem(user, store);

        /* (region 클래스에 Getter 필요
        if (store.getLimitType() == StoreLimitType.LIMITED) {
        Long regionId = store.getRegion().getRegionId();

        // 점령 여부 확인(reion_id 기준)]
        boolean conquerd = conquerRepository.existsByUserAndRegionId(user, regionId);
        if (!conquerd) {
            throw new IllegalStateException("해당 지역을 점령 후 한정판 아이템 구매가능합니다.");
        }
    }
        */



        // "구매 정보" 포인트 서비스로 전달
        /*PointSpendRequest request = PointSpendRequest.builder()
                .userId(user.getUserId())
                .sourceType("STORE")                    // ENUM('CAPTURE','LIKE','STORE')
                .sourceId(store.getItemId())            // 어떤 아이템 구매인지
                .pointValue(store.getPrice())           // 얼마짜리인지
                .description(store.getName())           // 어떤 아이템인지 (로그용)
                .build();

        // ⚙️ 포인트 차감 및 로그 기록
        pointService.handlePurchase(request);
        */

        return inventoryService.addToInventory(user, store);
    }
}