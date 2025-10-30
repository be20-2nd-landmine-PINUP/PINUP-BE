package pinup.backend.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import pinup.backend.member.entity.Users;
import pinup.backend.member.repository.MemberRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        // OAuth2User 정보 가져오기
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // google / kakao
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email;
        String name;
        String picture;

        // Google 로그인 처리
        if ("google".equals(registrationId)) {
            email = (String) attributes.get("email");
            name = (String) attributes.get("name");
            picture = (String) attributes.get("picture");
        }
        // Kakao 로그인 처리
        else if ("kakao".equals(registrationId)) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

            email = (String) kakaoAccount.get("email");
            name = (String) profile.get("nickname");
            picture = (String) profile.get("profile_image_url");
        } else {
            picture = null;
            email = null;
            name = null;
        }

        System.out.println(registrationId.toUpperCase() + " 로그인 사용자: " + email);

        // DB에 사용자 없으면 신규 생성
        memberRepository.findByEmail(email).orElseGet(() -> {
            Users newUser = Users.builder()
                    .loginType("google".equals(registrationId) ? Users.LoginType.GOOGLE : Users.LoginType.KAKAO)
                    .name(name)
                    .email(email)
                    .nickname(name)
                    .gender(Users.Gender.U)
                    .profileImage(picture)
                    .status(Users.Status.ACTIVE)
                    .preferredCategory(Users.PreferredCategory.자연)
                    .preferredSeason(Users.PreferredSeason.봄)
                    .birthDate(LocalDate.of(2000, 1, 1))
                    .build();
            return memberRepository.save(newUser);
        });

        // Google / Kakao 관계없이 공통된 필드 구조로 반환
        Map<String, Object> unifiedAttributes = new HashMap<>();
        unifiedAttributes.put("name", name);
        unifiedAttributes.put("email", email);
        unifiedAttributes.put("picture", picture);

        // 통합된 유저 정보 반환 (세션 저장용)
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                unifiedAttributes,
                "email"
        );
    }
}
