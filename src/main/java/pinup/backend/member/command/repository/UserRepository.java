package pinup.backend.member.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pinup.backend.member.command.domain.Users;

//회원 정지/삭제/상태변경
public interface UserRepository extends JpaRepository<Users, Long> {
}
