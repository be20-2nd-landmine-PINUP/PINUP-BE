package pinup.backend.store.query.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pinup.backend.store.query.dto.StoreResponseDto;
import pinup.backend.store.query.service.StoreQueryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store/query")
public class StoreQueryController {

    private final StoreQueryService storeQueryService;

    // 전체 아이템 목록 조회
    @GetMapping("/items")
    public List<StoreResponseDto> getActivceItems() {
        return storeQueryService.getActiveItems();
    }

    // 특정 아이템 상세 조회
    @GetMapping("/items/{itemId}")
    public StoreResponseDto getItemById(@PathVariable Integer itemId) {
        return storeQueryService.getItemById(itemId);
    }

}
