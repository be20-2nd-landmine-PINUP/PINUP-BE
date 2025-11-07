package pinup.backend.pinupnotice.notice.query.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.pinupnotice.notice.query.dto.NoticeListResponse;
import pinup.backend.pinupnotice.notice.query.dto.NoticeSpecificResponse;
import pinup.backend.pinupnotice.notice.query.mapper.NoticeMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeQueryService {
    private final NoticeMapper noticeMapper;

    @Transactional(readOnly = true)
    public List<NoticeListResponse> getAllNotices() {
        return noticeMapper.getNoticeList();
    }

    public NoticeSpecificResponse getNoticeById(Long id) {
        return noticeMapper.getNoticeSpecific(id);
    }
}
