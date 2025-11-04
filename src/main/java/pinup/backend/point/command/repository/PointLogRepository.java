package pinup.backend.point.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pinup.backend.point.command.domain.PointLog;
// JpaRepository는 Spring Data JPA가 자동으로 CRUD (Create, Read, Update, Delete) 기능을 만들어주는 인터페이스
// 단순 로그 테이블임으로 비어있는 인터페이스로.. 기본 CRUD만 있으면 충분
// 존재여부 조회 메서드 추가
public interface PointLogRepository extends JpaRepository<PointLog, Long> {
    boolean existsByUserIdAndSourceTypeAndPointSourceId(
            Long userId, PointLog.SourceType sourceType, Long pointSourceId
    );
}
