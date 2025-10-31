package pinup.backend.member.command.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pinup.backend.member.command.domain.Users;
import pinup.backend.member.command.service.MemberCommandService;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class MemberCommandController {

    private final MemberCommandService memberCommandService;

    @PostMapping("/member/update")
    public String updateMemberInfo(
            @RequestParam String email,
            @RequestParam String nickname,
            @RequestParam Users.Gender gender,
            @RequestParam Users.PreferredCategory preferredCategory,
            @RequestParam Users.PreferredSeason preferredSeason,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate
    ) {
        memberCommandService.updateMember(email, nickname, gender, preferredCategory, preferredSeason, birthDate);
        return "redirect:/mypage";
    }

    @PostMapping("/member/delete")
    public String deleteMember(@AuthenticationPrincipal OAuth2User oAuth2User) {
        if (oAuth2User != null) {
            String email = (String) oAuth2User.getAttributes().get("email");
            memberCommandService.deleteMember(email);
        }
        return "redirect:/login?deleted";
    }
}
