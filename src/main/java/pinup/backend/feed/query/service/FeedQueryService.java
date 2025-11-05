package pinup.backend.feed.query.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.feed.query.dto.FeedCardDto;
import pinup.backend.feed.query.dto.FeedQueryDto;
import pinup.backend.feed.query.dto.SliceResponse;
import pinup.backend.feed.query.mapper.FeedQueryMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedQueryService {

    private final FeedQueryMapper feedQueryMapper;

    public SliceResponse<FeedCardDto> getFeedSlice(int limit, String cursor) {
        // 커서 파싱 (첫 요청이면 null)
        Long cursorId = (cursor == null || cursor.isBlank()) ? null : Long.parseLong(cursor);

        // 파라미터 구성 (limit+1 조회로 hasNext 판단)
        Map<String, Object> params = new HashMap<>();
        params.put("cursorId", cursorId);
        params.put("limitPlusOne", limit + 1);

        // 조회
        List<FeedCardDto> rows = feedQueryMapper.selectFeedSlice(params);

        // hasNext 계산 + 초과 1개 제거
        boolean hasNext = rows.size() > limit;
        if (hasNext) {
            rows = rows.subList(0, limit);
        }

        // 5) nextCursor = 마지막 피드의 id
        String nextCursor = (hasNext && !rows.isEmpty())
                ? String.valueOf(rows.get(rows.size() - 1).id())
                : null;

        return new SliceResponse<>(rows, nextCursor, hasNext);
    }

    public FeedQueryDto getDetail(Long id) {
        FeedQueryDto dto = feedQueryMapper.selectFeedDetail(id);
        if (dto == null) {
            throw new IllegalArgumentException("존재하지 않는 피드입니다. id=" + id);
        }
        return dto;
    }
}
