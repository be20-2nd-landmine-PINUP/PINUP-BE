package pinup.backend.point.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pinup.backend.point.command.domain.TotalPoint;

import java.util.Optional;

@Repository
public interface TotalPointRepository extends JpaRepository<TotalPoint, Long> {

    /**
     * ✅ userId로 유저의 누적 포인트 조회
     */
    Optional<TotalPoint> findByUserId(Long userId);

    /**
     * ✅ 특정 유저의 누적 포인트 존재 여부 확인
     */
    boolean existsByUserId(Long userId);
}