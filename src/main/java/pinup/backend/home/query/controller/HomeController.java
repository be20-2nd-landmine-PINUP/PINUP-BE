package pinup.backend.home.query.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pinup.backend.member.command.domain.Users;
import pinup.backend.member.query.dto.UserDto;
import pinup.backend.member.query.service.UserQueryService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    private UserQueryService userQueryService;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal OAuth2User oAuth2User, Model model) {
        if (oAuth2User != null) {
            model.addAttribute("user", oAuth2User.getAttributes());
        }
        return "home"; // templates/home.html 렌더링
    }


}
