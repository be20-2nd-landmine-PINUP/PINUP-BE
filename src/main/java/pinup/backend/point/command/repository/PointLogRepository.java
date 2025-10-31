package pinup.backend.point.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pinup.backend.point.command.domain.PointLog;

public interface PointLogRepository extends JpaRepository<PointLog, Long> {}