package pinup.backend.store.query.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pinup.backend.store.command.dto.StoreDetailResponseDto;
import pinup.backend.store.command.dto.StoreSummaryResponseDto;
import pinup.backend.store.query.mapper.StoreMapper;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreQueryService {

    private final StoreMapper storeMapper;

    // 전체 아이템 조회
    public List<StoreSummaryResponseDto> getActiveItems() {
        return storeMapper.findAllActiveItems();
    }

    // 특정 아이템 조회
    public StoreDetailResponseDto getItemById(Integer itemId) {
        return storeMapper.findItemById(itemId);
    }
}
