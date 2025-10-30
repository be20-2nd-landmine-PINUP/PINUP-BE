package pinup.backend.member.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pinup.backend.member.command.domain.Users;

import java.util.Optional;

public interface MemberCommandRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
}
