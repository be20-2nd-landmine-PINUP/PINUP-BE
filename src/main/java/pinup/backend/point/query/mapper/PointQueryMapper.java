package pinup.backend.point.query.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pinup.backend.point.query.dto.PointLogResponse;

import java.util.List;
// mybatis 쿼리 연결
//sql, java 코드 연결용. 직접 sql을 xml로 작성하고, 자바객체(dto)로 매핑
// 즉, sql을 JPA 대신 직접 쓰게 함.
// 포인트 조회용 쿼리 모음
@Mapper
public interface PointQueryMapper {
    Integer selectTotalPoint(@Param("userId") Long userId);

    List<PointLogResponse> selectLogsByUser(
            @Param("userId") Long userId,
            @Param("limit") int limit,
            @Param("offset") int offset
    );
    //PARAM: xml 쿼리에서 이 파라미터를 사용할 것 의미. 쿼리 xml에서 자바 메서드로 치환
}
