package pinup.backend.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pinup.backend.member.entity.Users;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
}
