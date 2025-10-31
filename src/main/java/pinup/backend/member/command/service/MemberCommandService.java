package pinup.backend.member.command.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pinup.backend.member.command.domain.Users;
import pinup.backend.member.command.repository.MemberCommandRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MemberCommandService {

    private final MemberCommandRepository memberCommandRepository;

    @Transactional
    public void updateMember(String email, String nickname, Users.Gender gender,
                             Users.PreferredCategory preferredCategory,
                             Users.PreferredSeason preferredSeason,
                             LocalDate birthDate) {

        Users user = memberCommandRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        user.updateInfo(nickname, gender, preferredCategory, preferredSeason, birthDate);
    }

    @Transactional
    public void deleteMember(String email) {
        Users user = memberCommandRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
        user.delete();
    }
}
