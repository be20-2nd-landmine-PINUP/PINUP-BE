package pinup.backend.auth.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import pinup.backend.member.command.domain.Users;
import pinup.backend.member.command.repository.MemberCommandRepository;

import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService1 extends DefaultOAuth2UserService {

    private final MemberCommandRepository memberCommandRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String picture = (String) attributes.get("picture");

        System.out.println("Google 로그인 사용자: " + email);

        memberCommandRepository.findByEmail(email).orElseGet(() -> {
            Users newUser = Users.builder()
                    .loginType(Users.LoginType.GOOGLE)
                    .name(name)
                    .email(email)
                    .nickname(name)
                    .gender(Users.Gender.U)
                    .profileImage(picture)
                    .status(Users.Status.ACTIVE)
                    .preferredCategory(Users.PreferredCategory.자연)
                    .preferredSeason(Users.PreferredSeason.봄)
                    .birthDate(LocalDate.of(2000, 1, 1)) // 임시값
                    .build();
            return memberCommandRepository.save(newUser);
        });

        return oAuth2User;
    }
}
