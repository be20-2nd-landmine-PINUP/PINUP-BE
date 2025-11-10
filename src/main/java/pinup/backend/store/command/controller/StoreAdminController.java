package pinup.backend.store.command.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pinup.backend.store.command.domain.Store;
import pinup.backend.store.command.dto.StoreRequestDto;
import pinup.backend.store.command.service.StoreAdminService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store/admin")
@Tag(name = "Store Admin", description = "스토어 관리자 CRUD API")
public class StoreAdminController {

    private final StoreAdminService storeAdminService;

    // 아이템 등록
    @PostMapping("/items")
    @Operation(summary = "스토어 아이템 등록", description = "관리자가 새로운 스토어 아이템을 등록합니다.")
    public ResponseEntity<?> register(@RequestBody StoreRequestDto dto) {
        Store created = storeAdminService.registerItem(dto.getAdminId(), dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "itemId", created.getItemId(),
                        "message", "아이템 등록 완료"
                ));
    }

    //아이템 수정
    @PatchMapping("/items/{itemId}")
    @Operation(summary = "스토어 아이템 수정", description = "관리자가 기존 스토어 아이템을 수정합니다.")
    public ResponseEntity<?> updateItem(@PathVariable Integer itemId, @RequestBody StoreRequestDto dto) {
        Store updated = storeAdminService.updateItem(itemId, dto);
        return ResponseEntity.ok(Map.of(
                "itemId", updated.getItemId(),
                "message", "아이템 수정 완료"
        ));
    }

    //아이템 삭제
    @DeleteMapping("/items/{itemId}")
    @Operation(summary = "아이템 삭제", description = "관리자가 기존 스토어 아이템을 삭제합니다.")
    public ResponseEntity<?> deleteItem(@PathVariable Integer itemId) {
        storeAdminService.deleteItem(itemId);
        return ResponseEntity.ok(Map.of("message", "아이템 삭제 완료"));
    }
}
