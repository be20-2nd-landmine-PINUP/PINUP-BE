package pinup.backend.point;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.util.ReflectionTestUtils;
import pinup.backend.point.command.service.MonthlyBonusScheduler;
import pinup.backend.point.command.service.PointCommandService;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MonthlyBonusSchedulerTest {

    JdbcTemplate jdbc;
    @Mock
    PointCommandService pointService;  // ✅ @Mock (NOT @MockBean)
    MonthlyBonusScheduler scheduler;

    @BeforeEach
    void setUp() {
        var db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName("testdb;MODE=MySQL") // MySQL 모드
                .build();
        jdbc = new JdbcTemplate(db);

        // 고정 클럭 (2025-11-06 KST 기준 → 지난달=2025-10)
        ZoneId KST = ZoneId.of("Asia/Seoul");
        Clock fixedClock = Clock.fixed(
                LocalDateTime.of(2025, 11, 6, 11, 0).atZone(KST).toInstant(),
                KST
        );

        scheduler = new MonthlyBonusScheduler(jdbc, pointService, fixedClock);
        ReflectionTestUtils.setField(scheduler, "batchSize", 100);
        ReflectionTestUtils.setField(scheduler, "dryRun", false);

        // ✅ 스키마 생성 (테스트에 필요한 최소 필드만)
        jdbc.execute("""
        CREATE TABLE region (
            region_id BIGINT AUTO_INCREMENT PRIMARY KEY,
            region_depth1 VARCHAR(20),
            region_depth2 VARCHAR(20),
            region_depth3 VARCHAR(20)
        );
    """);

        jdbc.execute("""
        CREATE TABLE territory (
            territory_id INT AUTO_INCREMENT PRIMARY KEY,
            user_id INT NOT NULL,
            region_id BIGINT NOT NULL
        );
    """);

        jdbc.execute("""
        CREATE TABLE territory_visit_log (
            visit_id INT AUTO_INCREMENT PRIMARY KEY,
            territory_id INT NOT NULL,
            user_id BIGINT NOT NULL,
            is_valid BOOLEAN NOT NULL,
            visited_at TIMESTAMP NOT NULL
        );
    """);
    }


    @Test
    void grantMonthlyBonus_calls_service() {
        // given: (서울|강남|역삼) 지난달 방문 2건(<=100), 방문자 2명(10,11)
        Long rId = insertRegion("서울특별시", "강남구", "역삼동");
        Long t1 = insertTerritory(100, rId);
        Long t2 = insertTerritory(101, rId);

        insertVisit(t1, 10L, true, LocalDateTime.of(2025, 10, 10, 12, 0));
        insertVisit(t2, 11L, true, LocalDateTime.of(2025, 10, 25, 18, 30));

        // 다른 구역: (서울|중구|무학동) 방문 101건 → 제외
        Long r2 = insertRegion("서울특별시", "중구", "무학동");
        Long t3 = insertTerritory(200, r2);
        for (int i = 0; i < 101; i++) {
            insertVisit(t3, 1000L + i, true, LocalDateTime.of(2025, 10, 5, 9, 0));
        }

        // when
        System.out.println("▶ [보너스 스케줄러 테스트 시작]");
        System.out.println(" - 입력 데이터: 역삼동 방문자 2명, 무학동 방문자 101명");
        scheduler.grantMonthlyBonus();

        // then: 역삼동의 두 사용자에게만 호출
        verify(pointService).grantMonthlyBonusByDepth3(eq(10L), eq("서울특별시"), eq("강남구"), eq("역삼동"), eq(202510));
        verify(pointService).grantMonthlyBonusByDepth3(eq(11L), eq("서울특별시"), eq("강남구"), eq("역삼동"), eq(202510));
        verifyNoMoreInteractions(pointService);
        System.out.println("✅ [테스트 성공]");
        System.out.println("   ├─ 대상 구역 : 서울특별시 강남구 역삼동");
        System.out.println("   ├─ 지급 조건 : 지난달 방문자 수 ≤ 100");
        System.out.println("   ├─ 지급 인원 : 2명 (userId=10,11)");
        System.out.println("   ├─ 지급 포인트 : +10점");
        System.out.println("   └─ 호출 메서드 : grantMonthlyBonusByDepth3()");
    }

    // ---- helpers ----
    private Long insertRegion(String d1, String d2, String d3) {
        jdbc.update("INSERT INTO region(region_depth1,region_depth2,region_depth3) VALUES(?,?,?)", d1, d2, d3);
        return jdbc.queryForObject("SELECT MAX(region_id) FROM region", Long.class);
    }

    private Long insertTerritory(int userId, Long regionId) {
        jdbc.update("INSERT INTO territory(user_id, region_id) VALUES(?,?)", userId, regionId);
        return jdbc.queryForObject("SELECT MAX(territory_id) FROM territory", Long.class);
    }

    private void insertVisit(Long territoryId, Long userId, boolean valid, LocalDateTime ts) {
        jdbc.update("INSERT INTO territory_visit_log(territory_id,user_id,is_valid,visited_at) VALUES(?,?,?,?)",
                territoryId, userId, valid, Timestamp.valueOf(ts));
    }

}
