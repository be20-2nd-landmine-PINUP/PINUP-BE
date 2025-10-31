package pinup.backend.store.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pinup.backend.store.entity.Store;
import pinup.backend.store.repository.StoreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    /*
     *  상점 관련 비즈니스 로직
     * - 지역 한정 아이템 조회
     */

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

}
