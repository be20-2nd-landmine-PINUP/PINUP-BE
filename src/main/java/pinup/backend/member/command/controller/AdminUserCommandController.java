package pinup.backend.member.command.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pinup.backend.member.command.service.UserCommandService;

//회원 정지/활성화/삭제
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUserCommandController {

    private final UserCommandService userCommandService;

    @PostMapping("/{id}/suspend")
    public String suspendUser(@PathVariable Long id) {
        userCommandService.suspendUser(id);
        return "redirect:/admin/users"; // 목록으로 리다이렉트
    }

    @PostMapping("/{id}/activate")
    public String activateUser(@PathVariable Long id) {
        userCommandService.activateUser(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userCommandService.deleteUser(id);
        return "redirect:/admin/users";
    }
}
