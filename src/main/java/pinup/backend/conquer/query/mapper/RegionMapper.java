package pinup.backend.conquer.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pinup.backend.conquer.command.domain.entity.Region;

@Mapper
public interface RegionMapper {
    Region findRegion(@Param("longitude") double longitude, @Param("latitude") double latitude);
    Region findById(@Param("regionId") Long regionId);
}
