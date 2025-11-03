package pinup.backend.member.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pinup.backend.member.command.domain.Users;

import java.util.List;
import java.util.Optional;

//회원 등록/조회
public interface MemberCommandRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);

    List<Users> findByStatus(Users.Status status);

}
