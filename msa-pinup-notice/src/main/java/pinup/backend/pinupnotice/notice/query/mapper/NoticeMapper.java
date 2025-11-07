package pinup.backend.pinupnotice.notice.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pinup.backend.pinupnotice.notice.query.dto.NoticeListResponse;
import pinup.backend.pinupnotice.notice.query.dto.NoticeSpecificResponse;

import java.util.List;

@Mapper
public interface NoticeMapper {
    List<NoticeListResponse> getNoticeList();

    NoticeSpecificResponse getNoticeSpecific(@Param("noticeId") Long id);
}
