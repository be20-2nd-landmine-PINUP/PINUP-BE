package pinup.backend.conquer.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pinup.backend.conquer.command.domain.entity.Region;
import pinup.backend.conquer.command.domain.entity.Territory;
import pinup.backend.member.command.domain.Users;
import pinup.backend.ranking.MonthlyCaptureRankView;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface TerritoryRepository extends JpaRepository<Territory, Long> {
    boolean existsByUserIdAndRegion(Users userId, Region region);
    /**
     * (1) 100위 컷 산출용: 정렬 동일, PageRequest.of(99, 1)로 100번째 1건만 조회
     */
    @Query("""
        select t.userId.userId as userId,
               count(distinct t.region.regionId) as captureCount,
               max(t.captureEndAt) as lastCaptureAt
        from Territory t
        where t.captureEndAt is not null
          and t.captureEndAt >= :start
          and t.captureEndAt < :end
        group by t.userId.userId
        order by count(distinct t.region.regionId) desc,
                 max(t.captureEndAt) asc,
                 t.userId.userId asc
    """)
    List<MonthlyCaptureRankView> findMonthlyTop100DistinctRegion(
            @Param("start") Date start,
            @Param("end") Date end,
            Pageable pageable
    );

    /**
     * (2) 동점 포함 본 조회: cutoff 이상(HAVING >= :minCount) 전부 가져오기
     */
    @Query("""
        select t.userId.userId as userId,
               count(distinct t.region.regionId) as captureCount,
               max(t.captureEndAt) as lastCaptureAt
        from Territory t
        where t.captureEndAt is not null
          and t.captureEndAt >= :start and t.captureEndAt < :end
        group by t.userId.userId
        having count(distinct t.region.regionId) >= :minCount
        order by count(distinct t.region.regionId) desc,
                 max(t.captureEndAt) asc,
                 t.userId.userId asc
    """)
    List<MonthlyCaptureRankView> findMonthlyRankWithMinCount(
            @Param("start") Date start,
            @Param("end") Date end,
            @Param("minCount") long minCount
    );

}
