package pinup.backend.member.query.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pinup.backend.member.command.domain.Users;
import pinup.backend.member.query.dto.MemberResponse;
import pinup.backend.member.query.service.MemberQueryService;

@Controller
@RequiredArgsConstructor
public class MemberQueryController {

    private final MemberQueryService memberQueryService;

    @GetMapping("/mypage")
    public String myPage(@AuthenticationPrincipal OAuth2User oAuth2User, Model model) {
        if (oAuth2User != null) {
            String email = (String) oAuth2User.getAttributes().get("email");
            MemberResponse user = memberQueryService.getMemberInfoByEmail(email);
            model.addAttribute("user", user);
        }

        // 성별, 카테고리, 날씨 ENUM 값 전달
        model.addAttribute("genders", Users.Gender.values());
        model.addAttribute("categories", Users.PreferredCategory.values());
        model.addAttribute("seasons", Users.PreferredSeason.values());

        return "mypage";
    }
}
