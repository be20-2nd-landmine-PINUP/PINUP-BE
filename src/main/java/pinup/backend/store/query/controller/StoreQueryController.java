package pinup.backend.store.query.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pinup.backend.store.command.dto.StoreDetailResponseDto;
import pinup.backend.store.command.dto.StoreSummaryResponseDto;
import pinup.backend.store.query.service.StoreQueryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
@Tag(name = "store query", description = "스토어 조회 API")
public class StoreQueryController {

    private final StoreQueryService storeQueryService;

    // 전체 아이템 목록 조회
    @GetMapping("/items")
    @Operation(summary = "스토어 아이템 목록 조회", description = "판매 중인 스토어 아이템을 조회합니다.")
    public List<StoreSummaryResponseDto> getActivceItems() {
        return storeQueryService.getActiveItems();
    }

    // 특정 아이템 상세 조회
    @GetMapping("/items/{itemId}")
    @Operation(summary = "스토어 아이템 상세 조회", description = "특정 스토어 아이템의 상제 정보를 조회합니다.")
    public StoreDetailResponseDto getItemById(@PathVariable Integer itemId) {
        return storeQueryService.getItemById(itemId);
    }

}
