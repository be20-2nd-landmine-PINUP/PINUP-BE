package pinup.backend.point.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pinup.backend.point.command.domain.PointLog;

import java.util.Optional;

// JpaRepository는 Spring Data JPA가 자동으로 CRUD (Create, Read, Update, Delete) 기능을 만들어주는 인터페이스
// 단순 로그 테이블임으로 비어있는 인터페이스로.. 기본 CRUD만 있으면 충분
// 존재여부 조회 메서드 추가
public interface PointLogRepository extends JpaRepository<PointLog, Long> {
    // event_key 기반 멱등 체크 (UNIQUE 제약 활용)
    boolean existsByEventKey(String eventKey);

    // 필요 시 조회용
    Optional<PointLog> findByEventKey(String eventKey);
}
