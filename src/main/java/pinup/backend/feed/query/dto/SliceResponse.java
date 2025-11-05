package pinup.backend.feed.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

// FeedCard를 4장씩 담는 슬라이스(한줄씩)

@Getter
@AllArgsConstructor
@Schema(description = "커서 기반 무한 스크롤 응답")
public class SliceResponse<T> {

    @Schema(description = "현재 요청에서 가져온 데이터 목록")
    private final List<T> items;

    @Schema(description = "다음 페이지 요청 시 사용할 커서 (없으면 null)")
    private final String nextCursor;

    @Schema(description = "다음 데이터가 존재하는지 여부", example = "true")
    private final boolean hasNext;
}
