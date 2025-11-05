package pinup.backend.notice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.member.command.domain.Admin;
import pinup.backend.member.command.repository.AdminRepository;
import pinup.backend.notice.command.dto.NoticePostRequest;
import pinup.backend.notice.command.entity.Notice;
import pinup.backend.notice.command.repository.NoticeRepository;
import pinup.backend.notice.command.service.NoticeCommandService;
import pinup.backend.notice.query.dto.NoticeListResponse;
import pinup.backend.notice.query.dto.NoticeSpecificResponse;
import pinup.backend.notice.query.service.NoticeQueryService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class NoticeQueryTest {

    @Autowired
    private NoticeQueryService noticeQueryService;

    @Autowired
    private NoticeCommandService noticeCommandService;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private AdminRepository adminRepository;


    private Admin admin;

    @BeforeEach
    void setUp() {
        // 테스트 데이터용 관리자 생성 및 저장
        admin = Admin.builder()
                .password("password")
                .name("Test Admin")
                .status(Admin.Status.ACTIVE)
                .build();

        admin = adminRepository.save(admin);


        // 공지사항 테스트 데이터 생성 및 저장
        Notice notice1 = Notice.builder()
                .noticeTitle("Test Title 1")
                .noticeContent("Test Content 1")
                .admin(admin)
                .build();
        noticeRepository.save(notice1);

        Notice notice2 = Notice.builder()
                .noticeTitle("Test Title 2")
                .noticeContent("Test Content 2")
                .admin(admin)
                .build();
        noticeRepository.save(notice2);
    }


    /*
    * 관리자(Admin) db 테이블에 데이터가 존재해야 테스트 통과함
    */
    @Test
    @DisplayName("공지사항이 등록된다")
    void postNoticeTest() {
        NoticePostRequest request = NoticePostRequest.builder()
                .adminId(1L)
                .noticeTitle("Test Title 3")
                .noticeContent("Test Content 3")
                .build();
        Long id = noticeCommandService.postNotice(request);

        assertThat(noticeRepository.findById(id).get().getNoticeTitle()).isEqualTo("Test Title 3");
    }

    @Test
    @DisplayName("공지사항 목록이 조회된다")
    void getAllNoticeTest() {
        // when
        List<NoticeListResponse> notices = noticeQueryService.getAllNotices();

        // then
        assertThat(notices).isNotNull();
        assertThat(notices.getFirst().getNoticeId()).isNotNull();
        assertThat(notices.getFirst().getNoticeTitle()).isNotNull();
        assertThat(notices.getFirst().getCreatedAt()).isNotNull();

        notices.forEach(System.out::println);
    }

    @Test
    @DisplayName("공지사항 상세가 조회된다")
    void getSpecificNoticeTest() {
        // given
        Notice notice = Notice.builder()
                .noticeTitle("Specific Title")
                .noticeContent("Specific Content")
                .admin(admin)
                .build();
        noticeRepository.save(notice);
        Long noticeId = notice.getNoticeId();

        // when
        NoticeSpecificResponse foundNotice = noticeQueryService.getNoticeById(noticeId);

        // then
        assertThat(foundNotice).isNotNull();
        assertThat(foundNotice.getNoticeTitle()).isEqualTo("Specific Title");
        assertThat(foundNotice.getNoticeContent()).isEqualTo("Specific Content");
    }
}