package pinup.backend.store.command.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pinup.backend.store.command.domain.Store;
import pinup.backend.store.command.dto.StoreRequestDto;
import pinup.backend.store.command.service.StoreService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store/admin")
public class StoreAdminController {

    private final StoreService storeService;

    // 아이템 등록
    @PostMapping("/items")
    public ResponseEntity<?> register(@RequestBody StoreRequestDto dto) {
        Store created = storeService.registerItem(dto.getAdminId(), dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "itemId", created.getItemId(),
                        "message", "아이템 등록 완료"
                ));
    }

    //아이템 수정
    @PatchMapping("/items/{itemId}")
    public ResponseEntity<?> updateItem(@PathVariable Integer itemId, @RequestBody StoreRequestDto dto) {
        Store updated = storeService.updateItem(itemId, dto);
        return ResponseEntity.ok(Map.of(
                "itemId", updated.getItemId(),
                "message", "아이템 수정 완료"
        ));
    }

    //아이템 삭제
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable Integer itemId) {
        storeService.deleteItem(itemId);
        return ResponseEntity.ok(Map.of("message", "아이템 삭제 완료"));
    }
}
