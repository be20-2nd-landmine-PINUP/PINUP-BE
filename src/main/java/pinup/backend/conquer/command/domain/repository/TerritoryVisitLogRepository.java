package pinup.backend.conquer.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pinup.backend.conquer.command.domain.entity.TerritoryVisitLog;

public interface TerritoryVisitLogRepository extends JpaRepository<TerritoryVisitLog, Long> {
}
