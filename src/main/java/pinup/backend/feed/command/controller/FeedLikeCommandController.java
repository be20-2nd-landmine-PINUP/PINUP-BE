package pinup.backend.feed.command.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pinup.backend.feed.command.service.FeedLikeCommandService;
import pinup.backend.feed.common.response.ApiResponse;

@Tag(name = "Feed Like API", description = "피드 좋아요 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/feeds/{feedId}/likes")
public class FeedLikeCommandController {

    private final FeedLikeCommandService likeService;

    @Operation(summary = "피드 좋아요", description = "해당 피드에 좋아요를 1회 누릅니다. (중복 불가, 취소 없음)")
    @PostMapping
    public ApiResponse<FeedLikeCommandService.LikeResult> like(
            @PathVariable Long feedId,
            @RequestParam Long userId // 임시: 세션/Principal로 대체 예정
    ) {
        var result = likeService.like(feedId, userId);
        return ApiResponse.success(result, "좋아요 성공");
    }
}
