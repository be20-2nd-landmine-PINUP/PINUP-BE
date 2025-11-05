    package pinup.backend.feed.query.controller;

    import io.swagger.v3.oas.annotations.Operation;
    import io.swagger.v3.oas.annotations.Parameter;
    import io.swagger.v3.oas.annotations.tags.Tag;
    import jakarta.validation.constraints.Min;
    import lombok.RequiredArgsConstructor;
    import org.springframework.validation.annotation.Validated;
    import org.springframework.web.bind.annotation.*;
    import pinup.backend.feed.query.dto.FeedCardDto;
    import pinup.backend.feed.query.dto.FeedQueryDto;
    import pinup.backend.feed.query.dto.SliceResponse;
    import pinup.backend.feed.query.service.FeedQueryService;
    import pinup.backend.feed.common.response.ApiResponse;

    @Tag(name = "Feed Query")
    @RestController
    @RequiredArgsConstructor
    @RequestMapping("/feeds")
    @Validated
    public class FeedQueryController {

        private final FeedQueryService feedQueryService;

        @Operation(
                summary = "피드 리스트 조회",
                description = "한 줄(4개) 단위로 피드 목록을 조회합니다. 다음 요청 시 응답의 nextCursor 값을 cursor로 전달하세요."
        )
        @GetMapping("/list")
        public ApiResponse<SliceResponse<FeedCardDto>> getFeedList(
                @Parameter(description = "가져올 줄 수(한 줄 = 4개)", example = "1")
                @RequestParam(defaultValue = "1") @Min(1) int rows,
                @Parameter(description = "다음 페이지 커서 (첫 요청이면 생략)", example = "42")
                @RequestParam(required = false) String cursor
        ) {
            int limit = Math.max(1, rows) * 4;
            SliceResponse<FeedCardDto> result = feedQueryService.getFeedSlice(limit, cursor);
            return ApiResponse.success(result, "피드 목록 조회 성공");
        }

        @Operation(
                summary = "피드 상세 조회"
        )
        @GetMapping("/view/{id}")
        public ApiResponse<FeedQueryDto> getFeedDetail(@PathVariable Long id) {
            var dto = feedQueryService.getDetail(id);
            return ApiResponse.success(dto, "피드 상세 조회 성공");
        }
    }
