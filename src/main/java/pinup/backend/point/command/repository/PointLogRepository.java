package pinup.backend.point.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pinup.backend.point.command.domain.PointLog;

import java.util.List;

public interface PointLogRepository extends JpaRepository<PointLog, Integer> {
    boolean existsByEventKey(String eventKey);

    List<PointLog> findByUser_UserIdOrderByCreatedAtDesc(Long userId);
}