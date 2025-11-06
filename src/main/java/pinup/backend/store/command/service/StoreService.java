package pinup.backend.store.command.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.member.command.domain.Users;
import pinup.backend.store.command.domain.Inventory;
import pinup.backend.store.command.domain.Store;
import pinup.backend.store.command.repository.StoreRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreService {

    private final StoreRepository storeRepository;
    private final InventoryService inventoryService;
    //private final ConquerRepository conquerRepository;
    //private final PointService pointService;


    // 아이템 구매(포인트 모듈 호출)
    public Inventory purchaseItem(Users user, Integer itemId) {
        // 아이템 조회
        Store store = storeRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이템입니다."));

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

        //  카테고리별 추가 로직
        switch (store.getCategory()) {
            case MARKER:        // 지도에 핀/깃발 표시 아이템
                break;
            case SPECIALTY:     // 지역 특산물 이모지 장식 아이템
                break;
            case TILE:          // 보드 타일 스킨 교체 아이템
                break;
            case BUILDING:      // 건물 타입 아이템
                break;
        }

        // "구매 정보" 포인트 서비스로 전달
        /*PointSpendRequest request = PointSpendRequest.builder()
                .userId(user.getUserId())
                .sourceType("STORE")                    // ENUM(MARKER, SPECIALTY, BUILDING, TILE)
                .sourceId(store.getItemId())            // 어떤 아이템 구매인지
                .pointValue(store.getPrice())           // 얼마짜리인지
                .description(store.getName())           // 어떤 아이템인지 (로그용)
                .build();

        // ⚙️ 포인트 차감 및 로그 기록
        pointService.handlePurchase(request);
        */

        // 인벤토리 등록
        return inventoryService.addToInventory(user, store);
    }
}