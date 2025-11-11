package pinup.backend.conquer.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pinup.backend.conquer.command.domain.entity.TerritoryVisitLog;

import java.util.Date;

public interface TerritoryVisitLogRepository extends JpaRepository<TerritoryVisitLog, Long> {
    /**
     * 특정 지역(regionId)의 '기간 내' 유효 방문자(유저) 수 집계 - point용
     * - v.territoryId  : Territory 엔티티
     * - t.userId       : Users 엔티티 (주의: 필드명이 userId 이지만 타입은 Users)
     * - t.userId.userId: Users의 PK(Long) 접근
     * - t.region       : Region 엔티티 → t.region.regionId 로 PK 접근
     */
    @Query("""
        select count(distinct t.userId.userId)
        from TerritoryVisitLog v
          join v.territoryId t
        where t.region.regionId = :regionId
          and v.isValid = true
          and v.visitedAt between :start and :end
    """)
    long countDistinctVisitors(
            @Param("regionId") Long regionId,
            @Param("start") Date start,
            @Param("end") Date end
    );
}
