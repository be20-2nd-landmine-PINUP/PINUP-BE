package pinup.backend.store.query.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pinup.backend.store.query.dto.StoreResponseDto;
import pinup.backend.store.query.mapper.StoreMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreQueryService {

    private final StoreMapper storeMapper;

    // 전체 아이템 조회
    public List<StoreResponseDto> getActiveItems() {
        return storeMapper.findAllActiveItems();
    }

    // 특정 아이템 조회
    public StoreResponseDto getItemById(Integer itemId) {
        return storeMapper.findItemById(itemId);
    }
}
