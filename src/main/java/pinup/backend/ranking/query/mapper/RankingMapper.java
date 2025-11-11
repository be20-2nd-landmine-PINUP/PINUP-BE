package pinup.backend.ranking.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pinup.backend.ranking.query.dto.MonthlyRankDto;

import java.util.List;

@Mapper
public interface RankingMapper {
    List<MonthlyRankDto> selectMonthlyTop100WithTies(
            @Param("start") java.time.LocalDateTime start,
            @Param("endDate") java.time.LocalDateTime endDate
    );
}