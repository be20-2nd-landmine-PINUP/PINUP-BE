package pinup.backend.member.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pinup.backend.member.command.domain.Admin;

import java.util.Optional;

//관리자 로그인, 초기 계정 생성
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByName(String name);
}
