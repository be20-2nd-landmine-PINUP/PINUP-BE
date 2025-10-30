package pinup.backend.notice.query.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.notice.query.dto.NoticeListResponse;
import pinup.backend.notice.query.dto.NoticeSpecificResponse;

import java.util.List;

@Service
@Transactional
public class NoticeService {
    public List<NoticeListResponse> getAllNotices() {
        return null;
    }

    public NoticeSpecificResponse getNoticeById(Integer id) {
        return null;
    }
}
