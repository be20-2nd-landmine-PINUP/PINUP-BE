package pinup.backend.feed.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pinup.backend.feed.query.dto.FeedCardDto;
import pinup.backend.feed.query.dto.FeedQueryDto;

import java.util.List;
import java.util.Map;

@Mapper
public interface FeedQueryMapper {

    /** 카드 리스트 */
    List<FeedCardDto> selectFeedSlice(Map<String, Object> params);
    // params: cursorId(Long, nullable), limitPlusOne(int)

    /** 피드 상세 보기 Mapper */
    FeedQueryDto selectFeedDetail(@Param("id") Long id);
}
