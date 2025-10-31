package pinup.backend.member.query.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pinup.backend.member.command.domain.Users;
import pinup.backend.member.command.repository.MemberCommandRepository;
import pinup.backend.member.query.dto.MemberResponse;

@Service
@RequiredArgsConstructor
public class MemberQueryService {

    private final MemberCommandRepository memberCommandRepository;

    public MemberResponse getMemberInfoByEmail(String email) {
        Users user = memberCommandRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
        return MemberResponse.from(user);
    }
}
