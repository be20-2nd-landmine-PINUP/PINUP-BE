package pinup.backend.point.query.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.point.query.dto.PointBalanceResponse;
import pinup.backend.point.query.dto.PointLogResponse;
import pinup.backend.point.query.mapper.PointQueryMapper;

import java.util.List;

@Service
public class PointQueryService {

    private final PointQueryMapper mapper;

    public PointQueryService(PointQueryMapper mapper) {
        this.mapper = mapper;
    }

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
