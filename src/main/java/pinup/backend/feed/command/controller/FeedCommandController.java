package pinup.backend.feed.command.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pinup.backend.feed.command.dto.FeedCreateRequest;
import pinup.backend.feed.command.dto.FeedUpdateRequest;
import pinup.backend.feed.command.service.FeedCommandService;

@Tag(name = "Feed Command API", description = "피드 작성/수정/삭제 API")
@RestController
@RequestMapping("/feeds")
@RequiredArgsConstructor
public class FeedCommandController {

    private final FeedCommandService feedCommandService;

    @Operation(summary = "피드 작성", description = "새로운 피드를 작성합니다.")
    @PostMapping("/write")
    public ResponseEntity<ApiResponse<Long>> createFeed(@RequestBody FeedCreateRequest request) {
        Long feedId = feedCommandService.createFeed(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(feedId, "피드 생성 성공"));
    }

    @Operation(summary = "피드 수정", description = "기존 피드를 수정합니다.")
    @PutMapping("/modify/{feedId}")
    public ResponseEntity<ApiResponse<Void>> updateFeed(
            @PathVariable Long feedId,
            @RequestBody FeedUpdateRequest request) {
        feedCommandService.updateFeed(feedId, request);
        return ResponseEntity
                .ok(ApiResponse.success("피드 수정 성공"));
    }

    @Operation(summary = "피드 삭제", description = "지정된 피드를 삭제합니다.")
    @DeleteMapping("/delete/{feedId}")
    public ResponseEntity<ApiResponse<Void>> deleteFeed(@PathVariable Long feedId) {
        feedCommandService.deleteFeed(feedId);
        return ResponseEntity
                .ok(ApiResponse.success("피드 삭제 성공"));
    }
}