package pinup.backend.pinupnotice.notice.command.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.pinupnotice.notice.command.dto.NoticePostRequest;
import pinup.backend.pinupnotice.notice.command.entity.Notice;
import pinup.backend.pinupnotice.notice.command.repository.NoticeRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeCommandService {

    private final NoticeRepository noticeRepository;

    public Long postNotice(NoticePostRequest request) {
        Long adminId = request.getAdminId();


        return noticeRepository.save(Notice.builder()
                .noticeContent(request.getNoticeContent())
                .noticeTitle(request.getNoticeTitle())
                .adminId(adminId)
                .build()).getNoticeId();
    }
}
