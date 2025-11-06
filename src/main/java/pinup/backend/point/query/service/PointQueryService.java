package pinup.backend.point.query.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.point.query.mapper.PointQueryMapper;
import pinup.backend.point.query.dto.PointBalanceResponse;
import pinup.backend.point.query.dto.PointLogResponse;

import java.util.List;

/**
 * 포인트 조회 관련 비즈니스 로직을 담당하는 서비스 클래스.
 *
 * - MyBatis Mapper를 통해 DB에서 직접 조회 수행.
 * - JPA를 사용하지 않고, 읽기 전용 쿼리만 실행.
 */
@Service
public class PointQueryService {

    // DB 쿼리를 실행하는 MyBatis 매퍼를 의존성으로 주입
    private final PointQueryMapper mapper;

    public PointQueryService(PointQueryMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 포인트 잔액 조회
     *
     * 특정 유저의 현재 총 포인트 합계를 조회하여 DTO로 반환한다.
     * 값이 null인 경우(기록 없음)는 0으로 처리.
     *
     * @param userId 사용자 ID
     * @return 사용자의 포인트 잔액 응답 DTO
     */
    @Transactional(readOnly = true)
    public PointBalanceResponse getBalance(Long userId) {
        Integer total = mapper.selectTotalPoint(userId);
        return new PointBalanceResponse(userId, total == null ? 0 : total);
    }

    /**
     * 포인트 로그 조회
     *
     * 특정 유저의 포인트 적립/차감 내역을 페이지 단위로 조회한다.
     *
     * @param userId 사용자 ID
     * @param limit  조회할 최대 개수
     * @param offset 조회 시작 위치
     * @return 포인트 로그 리스트 응답
     */
    @Transactional(readOnly = true)
    public List<PointLogResponse> getLogs(Long userId, int limit, int offset) {
        return mapper.selectLogsByUser(userId, limit, offset);
    }
}
