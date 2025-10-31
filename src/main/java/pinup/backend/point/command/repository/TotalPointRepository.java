package pinup.backend.point.command.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.point.command.domain.TotalPoint;

public interface TotalPointRepository extends JpaRepository<TotalPoint, Long> {

    // MySQL 원자적 차감 (성공 시 1, 실패 시 0)
    @Modifying
    @Transactional
    @Query(value = """
        UPDATE total_point
           SET total_point = total_point - :value
         WHERE user_id = :userId
           AND total_point >= :value
        """, nativeQuery = true)
    int trySubtract(@Param("userId") Long userId, @Param("value") int value);

    // MySQL Upsert: 누적 가산
    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO total_point (user_id, total_point)
        VALUES (:userId, :value)
        ON DUPLICATE KEY UPDATE total_point = total_point + VALUES(total_point)
        """, nativeQuery = true)
    int upsertAdd(@Param("userId") Long userId, @Param("value") int value);
}
