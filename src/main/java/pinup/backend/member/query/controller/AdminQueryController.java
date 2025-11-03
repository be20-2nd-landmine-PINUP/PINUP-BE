package pinup.backend.member.query.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pinup.backend.member.query.service.AdminQueryService;

@Controller
@RequiredArgsConstructor
public class AdminQueryController {

    private final AdminQueryService adminQueryService;

    // 관리자 홈 대시보드
    @GetMapping("/admin/home")
    public String showAdminHome(Model model) {
        // 전체 회원 수 조회
        model.addAttribute("userCount", adminQueryService.getUserCount());

        // 오늘 가입한 회원 수
        model.addAttribute("newUsersToday", adminQueryService.getNewUsersToday());

        return "admin/home";
    }
}
