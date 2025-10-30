package pinup.backend.notice.query.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.notice.query.dto.NoticeListResponse;
import pinup.backend.notice.query.dto.NoticeSpecificResponse;
import pinup.backend.notice.query.entity.Notice;
import pinup.backend.notice.query.repository.NoticeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {
    private final NoticeRepository noticeRepository;

    @Transactional(readOnly = true)
    public List<NoticeListResponse> getAllNotices() {
        List<Notice> notices =  noticeRepository.findAll();

        return notices.stream().map(
                notice -> NoticeListResponse.builder()
                        .noticeId(notice.getNoticeId())
                        .noticeTitle(notice.getNoticeTitle())
                        .build()
        ).toList();
    }

    @Transactional(readOnly = true)
    public NoticeSpecificResponse getNoticeById(Integer noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(IllegalArgumentException::new);

        return NoticeSpecificResponse.builder()
                .noticeId(notice.getNoticeId())
                .adminId(notice.getNoticeId())
                .noticeTitle(notice.getNoticeTitle())
                .noticeContent(notice.getNoticeContent())
                .createdAt(notice.getCreatedAt())
                .updatedAt(notice.getUpdatedAt())
                .build();
    }
}
