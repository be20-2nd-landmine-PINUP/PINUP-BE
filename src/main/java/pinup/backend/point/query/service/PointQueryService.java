package pinup.backend.point.query.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.point.query.dto.PointBalanceResponse;
import pinup.backend.point.query.dto.PointLogResponse;
import pinup.backend.point.query.mapper.PointQueryMapper;

import java.util.List;
// 포인트 잔액, 내역 조회(읽기) 기능
// service; 서비스 계층 bean으로 자동 등록.
@Service
public class PointQueryService {
    // db 쿼리를 실행하는 mybatis mapper를 의존성으로 주입
    private final PointQueryMapper mapper;

    public PointQueryService(PointQueryMapper mapper) {
        this.mapper = mapper;
    }
// 포인트 잔액 조회;; 특정 유저의 현재 총 포인트 합계 조회하여 dto로 반환
    @Transactional(readOnly = true)
    public PointBalanceResponse getBalance(Long userId) {
        Integer total = mapper.selectTotalPoint(userId);
        return new PointBalanceResponse(userId, total == null ? 0 : total);
    }

    @Transactional(readOnly = true)
    public List<PointLogResponse> getLogs(Long userId, int limit, int offset) {
        return mapper.selectLogsByUser(userId, limit, offset);
    }
}
