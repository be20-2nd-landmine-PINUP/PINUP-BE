package pinup.backend.store.command.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.member.command.domain.Admin;
import pinup.backend.member.command.domain.Users;
import pinup.backend.member.command.repository.AdminRepository;
import pinup.backend.store.command.domain.Inventory;
import pinup.backend.store.command.domain.Store;
import pinup.backend.store.command.dto.StoreRequestDto;
import pinup.backend.store.command.repository.StoreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreService {

    private final StoreRepository storeRepository;
    private final InventoryService inventoryService;
    private final AdminRepository adminRepository;
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

        //  카테고리별 분기(개별) 처리
        switch (store.getCategory()) {
            case MARKER:
                // 지도에 핀/깃발 표시 아이템
                break;
            case SPECIALTY:
                // 지역 특산물 이모지 장식 아이템
                break;
            case TILE:
                // 보드 타일 스킨 교체 아이템
                break;
            case BUILDING:
                // 건물 타입 아이템
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

    // 아이템 등록(관리자 전용)
    public Store registerItem(Long adminId, StoreRequestDto dto) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("관리자를 찾을 수 없습니다."));

        Store store = Store.builder()
                .admin(admin)
                .region(dto.getRegion())
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .category(dto.getCategory())
                .limitType(dto.getLimitType())
                .imageUrl(dto.getImageUrl())
                .isActive(true)
                .build();

        return storeRepository.save(store);
    }

    // 아이템 수정(관리자 전용)
    public Store updateItem(Integer itemId, StoreRequestDto dto) {
        Store store = storeRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("아이템을 찾을 수 없습니다."));

        store.update(dto);
        return storeRepository.save(store);
    }

    // 아이템 삭제(관리자 전용
    public void deleteItem(Integer itemId) {
        Store store = storeRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템이 존재 하지 않습니다."));
        storeRepository.delete(store);
    }


}