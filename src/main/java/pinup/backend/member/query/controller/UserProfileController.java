package pinup.backend.member.query.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pinup.backend.member.query.dto.UserDto;
import pinup.backend.member.query.service.UserQueryService;

@Controller
@RequiredArgsConstructor
public class UserProfileController {

    private final UserQueryService userQueryService;

    @GetMapping("/users/{id}")
    public String showUserProfile(@PathVariable Integer id, Model model) {
        UserDto targetUser = userQueryService.getUserById(id);

        if (targetUser == null) {
            model.addAttribute("errorMessage", "존재하지 않는 사용자입니다.");
            return "error/404";
        }

        model.addAttribute("user", targetUser);
        return "member/user-profile";
    }
}
