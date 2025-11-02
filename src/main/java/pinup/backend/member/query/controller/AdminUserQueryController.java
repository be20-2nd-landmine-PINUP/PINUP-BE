package pinup.backend.member.query.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pinup.backend.member.query.service.UserQueryService;

//회원 조회용
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminUserQueryController {

    private final UserQueryService userQueryService;

    // 전체 회원 조회
    @GetMapping("/users")
    public String showUserList(Model model) {
        model.addAttribute("users", userQueryService.getAllUsers());
        return "admin/users";
    }

    // 정지된 회원만 조회
    @GetMapping("/users/suspended")
    public String showSuspendedUsers(Model model) {
        model.addAttribute("users", userQueryService.getSuspendedUsers());
        model.addAttribute("filter", "suspended"); // 뷰에서 상태 표시용
        return "admin/users";
    }
}
