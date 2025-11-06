package pinup.backend.notice.query.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pinup.backend.notice.query.service.NoticeQueryService;

@Controller
@RequiredArgsConstructor
public class NoticeViewController {

    private final NoticeQueryService noticeQueryService;

    // 관리자 공지 목록 페이지 (HTML 렌더링)
    @GetMapping("/admin/notices")
    public String getNoticeList(Model model) {
        model.addAttribute("noticeList", noticeQueryService.getAllNotices());
        return "notices"; // templates/admin/notices.html
    }
}
