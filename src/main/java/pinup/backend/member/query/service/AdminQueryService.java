package pinup.backend.member.query.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.member.query.mapper.AdminQueryMapper;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminQueryService {

    private final AdminQueryMapper adminQueryMapper;

    // 전체 회원 수
    public int getUserCount() { return adminQueryMapper.countUsers(); }

    // 오늘 가입한 회원 수
    public int getNewUsersToday() { return adminQueryMapper.countNewUsersToday(); }
}
