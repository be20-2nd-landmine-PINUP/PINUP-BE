package pinup.backend.store.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pinup.backend.store.query.dto.StoreResponseDto;

import java.util.List;

@Mapper
public interface StoreMapper {

    // 전체 아이템 조회
    List<StoreResponseDto> findAllActiveItems();

    // 특정 아이템 상세 조회
    StoreResponseDto findItemById(@Param("itemId") Integer itemId);

}
