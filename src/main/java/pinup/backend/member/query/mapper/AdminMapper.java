package pinup.backend.member.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {

    // 전체 회원 수 (모든 사용자)
    int countUsers();

    // 오늘 가입한 회원 수
    int countNewUsersToday();

}
