package pinup.backend.ranking.command.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Map;

/**
 * 쓰기(Command) 전용 랭킹 서비스
 * - 월별 랭킹 집계 / 재계산 등 데이터 변경 작업 수행
 */
@Service
@RequiredArgsConstructor
@Transactional
public class RankingCommandService {

    private final NamedParameterJdbcTemplate jdbc;

    @Value("${ranking.lock.enabled:true}") // 운영/개발 기본 true, 테스트에서 false
    private boolean lockEnabled;

    public void buildMonthlyRanking(YearMonth ym) {
        String lockKey = "rank:" + ym;
        Integer got = 1;

        if (lockEnabled) {
            try {
                got = jdbc.queryForObject("SELECT GET_LOCK(:key, 5)", Map.of("key", lockKey), Integer.class);
            } catch (Exception e) { got = 1; } // 테스트 DB/H2 대비
            if (got == null || got != 1) {
                throw new IllegalStateException("이미 해당 월 집계가 실행 중입니다.");
            }
        }

        try {
            // ... (DELETE FROM ranking ... + INSERT ... 쿼리는 기존 그대로)
        } finally {
            if (lockEnabled) {
                try { jdbc.queryForObject("SELECT RELEASE_LOCK(:key)", Map.of("key", lockKey), Integer.class); }
                catch (Exception ignore) {}
            }
        }
    }
}



/*
주요 기술 포인트
1) @Transactional; 전체 메서드를 하나의 트랜잭션으로 묶어 원자성 보장
2) NamedParameterJdbcTemplate: 복잡한 SQL도 안전하게 파라미터 바인딩 가능
3) MySQL GET_LOCK() / RELEASE_LOCK(); 같은 월에 대한 중복 랭킹 생성 방지 (분산락 효과)
4) DENSE_RANK(); 중복 순위 건너뛰지 않고 계산
5) ON DUPLICATE KEY UPDATE; 	기존 데이터가 있을 경우 덮어쓰기 (갱신 처리)
 */