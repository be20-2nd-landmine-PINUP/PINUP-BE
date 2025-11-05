package pinup.backend.member.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminQueryMapper {

    // 전체 회원 수 (모든 사용자)
    @Select("""
        SELECT COUNT(*)
        FROM users
    """)
    int countUsers();

    // 오늘 가입한 회원 수
    @Select("""
        SELECT COUNT(*)
        FROM users
        WHERE DATE(created_at) = CURDATE()
    """)
    int countNewUsersToday();

}
