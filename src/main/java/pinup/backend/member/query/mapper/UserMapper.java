package pinup.backend.member.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import pinup.backend.member.query.dto.UserDto;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("""
        SELECT
            user_id AS id,
            user_name AS name,
            nickname,
            email,
            status
        FROM users
        ORDER BY user_id DESC
    """)
    List<UserDto> findAllUsers();

    @Select("""
        SELECT
            user_id AS id,
            user_name AS name,
            nickname,
            email,
            status
        FROM users
        WHERE user_id = #{id}
    """)
    UserDto findUserById(int id);
}
