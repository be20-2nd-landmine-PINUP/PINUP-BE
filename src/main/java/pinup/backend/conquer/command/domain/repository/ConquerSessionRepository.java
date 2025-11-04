package pinup.backend.conquer.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pinup.backend.conquer.command.domain.entity.ConquerSession;

import java.util.Optional;

public interface ConquerSessionRepository extends JpaRepository<ConquerSession, Long> {
    Optional<ConquerSession> findByIdAndUserId(Long id, Long userId);
}
