package pinup.backend.member.command.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pinup.backend.member.command.domain.Users;
import pinup.backend.member.command.repository.MemberCommandRepository;
import pinup.backend.member.command.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandService {

    private final UserRepository userRepository;
    private final MemberCommandRepository memberCommandRepository;

    // 회원 정지
    public void suspendUser(Integer id) {
        Users user = memberCommandRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        user.suspend(); // Users 엔티티에 정의된 메서드
    }

    // 회원 활성화
    public void activateUser(Integer id) {
        Users user = memberCommandRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        user.activate();
    }

    // 회원 삭제
    public void deleteUser(Integer id) {
        Users user = memberCommandRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        user.delete();
    }
}
