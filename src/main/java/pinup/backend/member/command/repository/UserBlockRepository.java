package pinup.backend.member.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pinup.backend.member.command.domain.UserBlock;

import java.util.List;
import java.util.Optional;

public interface UserBlockRepository extends JpaRepository<UserBlock, Long> {
    Optional<UserBlock> findByBlockerIdAndBlockedId(Long blockerId, Long blockedId);
    List<UserBlock> findAllByBlockerId(Long blockerId);
}
