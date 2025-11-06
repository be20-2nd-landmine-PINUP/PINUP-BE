package pinup.backend.store.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import pinup.backend.store.command.dto.StoreDetailResponseDto;
import pinup.backend.store.command.dto.StoreSummaryResponseDto;

import java.util.List;

@Mapper
public interface StoreMapper {

    // 전체 아이템 조회
    List<StoreSummaryResponseDto> findAllActiveItems();

    // 특정 아이템 상세 조회
    StoreDetailResponseDto findItemById(Integer itemId);

}
