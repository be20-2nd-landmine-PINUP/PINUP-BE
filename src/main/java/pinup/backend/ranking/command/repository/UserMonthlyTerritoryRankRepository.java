package pinup.backend.ranking.command.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pinup.backend.ranking.command.domain.UserMonthlyTerritoryRank;
import pinup.backend.ranking.command.domain.UserMonthlyTerritoryRankKey;

import java.util.List;
//UserMonthlyTerritoryRank 엔티티를 위한 Repository 인터페이스
// JpaRepository<엔티티 클래스, 기본키 타입>을 상속하면
// 기본적인 CRUD 메서드(save, findAll, findById 등)를 자동으로 제공받는다.
public interface UserMonthlyTerritoryRankRepository
        extends JpaRepository<UserMonthlyTerritoryRank, UserMonthlyTerritoryRankKey> {
    // <특정 월의 상위 100명 active 유저 랭킹 조회
    //@Query 어노테이션을 이용한 네이티브 쿼리
    //user_monthly_territory_rank 테이블과 users 테이블을 조인.
    // 조건; year_month가 파라미터로 받은 ym과 일치해야 함.
    //      users.status = 'ACTIVE' 인 유저만 포함.
    //      rank_int ≤ 100 인 상위 100명의 랭킹만 조회.
    //  결과: List<UserMonthlyTerritoryRank> 형태로 반환.
    @Query(value = """
        SELECT r.*
        FROM user_monthly_territory_rank r
        JOIN users u ON u.user_id = r.user_id
        WHERE r.year_month = :ym
          AND u.status = 'ACTIVE'
          AND r.rank_int <= 100
        ORDER BY r.rank_int ASC, r.user_id ASC
        """, nativeQuery = true)
    List<UserMonthlyTerritoryRank> findTop100(@Param("ym") String yearMonth);

    // 특정 유저의 행당 월 랭킹 조회:  — EmbeddedId의 필드명을 활용한 파생 쿼리
    List<UserMonthlyTerritoryRank> findByIdYearMonthAndIdUserId(String yearMonth, Long userId);
}
