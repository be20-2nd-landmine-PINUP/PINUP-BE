package pinup.backend.ranking;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.Instant;
import java.util.List;

@Mapper
public interface RankingMapper {
    List<MonthlyRankDto> selectMonthlyTop100WithTies(
            @Param("start") Instant start,
            @Param("end")   Instant end
    );
}