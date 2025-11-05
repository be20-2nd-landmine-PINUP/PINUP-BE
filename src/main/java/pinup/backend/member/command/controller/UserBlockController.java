package pinup.backend.member.command.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pinup.backend.member.command.service.UserBlockService;
import pinup.backend.member.command.service.UserCommandService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserBlockController {

    private final UserBlockService userBlockService;
    private final UserCommandService userCommandService;

    // 차단
    @PostMapping("/{id}/block")
    public String blockUser(@PathVariable Long id, Principal principal) {
        Long loginUserId = userCommandService.getLoginUserId(principal);
        userBlockService.blockUser(loginUserId, id);
        return "redirect:/users/" + id;
    }

    // 차단 해제
    @PostMapping("/{id}/unblock")
    public String unblockUser(@PathVariable Long id, Principal principal) {
        Long loginUserId = userCommandService.getLoginUserId(principal);
        userBlockService.unblockUser(loginUserId, id);
        return "redirect:/users/" + id;
    }
}
