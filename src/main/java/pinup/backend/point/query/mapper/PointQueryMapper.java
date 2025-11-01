package pinup.backend.point.query.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pinup.backend.point.query.dto.PointLogResponse;

import java.util.List;

@Mapper
public interface PointQueryMapper {
    Integer selectTotalPoint(@Param("userId") Long userId);

    List<PointLogResponse> selectLogsByUser(
            @Param("userId") Long userId,
            @Param("limit") int limit,
            @Param("offset") int offset
    );
}
