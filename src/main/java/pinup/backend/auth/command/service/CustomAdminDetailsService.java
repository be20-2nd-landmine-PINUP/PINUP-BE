package pinup.backend.auth.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pinup.backend.member.command.domain.Admin;
import pinup.backend.member.command.repository.AdminRepository;

@Service
@RequiredArgsConstructor
public class CustomAdminDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("관리자 계정을 찾을 수 없습니다."));

        return User.builder()
                .username(admin.getName())
                .password(admin.getPassword())
                .roles("ADMIN")
                .build();
    }
}
