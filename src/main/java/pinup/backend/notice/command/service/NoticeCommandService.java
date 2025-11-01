package pinup.backend.notice.command.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;
import pinup.backend.member.command.domain.Admin;
import pinup.backend.member.command.repository.AdminRepository;
import pinup.backend.notice.command.dto.NoticePatchRequest;
import pinup.backend.notice.command.dto.NoticePatchResponse;
import pinup.backend.notice.command.dto.NoticePostRequest;
import pinup.backend.notice.command.entity.Notice;
import pinup.backend.notice.command.repository.NoticeRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeCommandService {

    private final NoticeRepository noticeRepository;
    private final AdminRepository adminRepository;

    public Integer postNotice(NoticePostRequest request) {
        Admin admin = adminRepository.findById((request.getAdminId())).orElseThrow(IllegalArgumentException::new);

        return noticeRepository.save(Notice.builder()
                .noticeContent(request.getNoticeContent())
                .noticeTitle(request.getNoticeTitle())
                .admin(admin)
                .build()).getNoticeId();
    }

    public NoticePatchResponse patchNotice(NoticePatchRequest request) {
        Notice notice = noticeRepository.save(Notice.builder()
                .noticeContent(request.getNoticeContent())
                .noticeTitle(request.getNoticeTitle())
                .build());

        return NoticePatchResponse.builder()
                .noticeId(notice.getNoticeId())
                .adminId(notice.getAdmin().getId())
                .noticeContent(notice.getNoticeContent())
                .noticeTitle(notice.getNoticeTitle())
                .createdAt(notice.getCreatedAt())
                .updatedAt(notice.getUpdatedAt())
                .build();
    }

    public Integer deleteNotice(Integer id) {
        Notice notice = noticeRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        return notice.deleteNotice();
    }
}
