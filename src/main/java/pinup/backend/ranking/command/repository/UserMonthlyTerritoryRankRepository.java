package pinup.backend.ranking.command.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pinup.backend.ranking.command.domain.UserMonthlyTerritoryRank;
import pinup.backend.ranking.command.domain.UserMonthlyTerritoryRankKey;

import java.util.List;

public interface UserMonthlyTerritoryRankRepository
        extends JpaRepository<UserMonthlyTerritoryRank, UserMonthlyTerritoryRankKey> {

    // users.status = 'ACTIVE' 조건이 필요하고, User와 연관매핑이 없다면 네이티브로 처리
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

    // 내 랭킹 한 건(또는 없으면 빈 리스트) — EmbeddedId의 필드명을 활용한 파생 쿼리
    List<UserMonthlyTerritoryRank> findByIdYearMonthAndIdUserId(String yearMonth, Long userId);
}
