package pinup.backend.point.command.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.point.command.domain.TotalPoint;

public interface TotalPointRepository extends JpaRepository<TotalPoint, Long> {

    // 포인트 차감; 결제용
    // WHERE절에 total_point >= :value 조건이 있어서 부족하면 차감 안 됨.
    // 포인트가 충분할 떄만 차감됨. 실행결과 1이면 차감 성공, 실패 0(포인트 부족)
    // ATOMIC: 동시에 여러 요청이 와도 한번만 차감됨.
    @Modifying // JPA에 UPDATE, INSERT쿼리임을 알림
    @Transactional // 한 쿼리 단위를 트랜잭션으로 묶어.
    @Query(value = """
        UPDATE total_point
           SET total_point = total_point - :value
         WHERE user_id = :userId
           AND total_point >= :value 
        """, nativeQuery = true)
    int trySubtract(@Param("userId") Long userId, @Param("value") int value);
    // nativeQuery = true; mysql의 sql문 그대로 사용
    //param(userid)는 쿼리의 userid와 연결

    // 포인트 적립; 좋아요/점령
    // USERID가 없으면 새로 INSERT, 이미 있으면 기존에 +value(누적 적립)
    // == UPSERT(UPDATE + INSERT)기능

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO total_point (user_id, total_point)
        VALUES (:userId, :value)
        ON DUPLICATE KEY UPDATE total_point = total_point + VALUES(total_point)
        """, nativeQuery = true)
    int upsertAdd(@Param("userId") Long userId, @Param("value") int value);
}
