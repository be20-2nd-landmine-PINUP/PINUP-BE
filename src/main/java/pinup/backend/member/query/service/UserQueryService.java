package pinup.backend.member.query.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pinup.backend.member.query.dto.UserDto;
import pinup.backend.member.query.mapper.UserMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserMapper userMapper;

    public List<UserDto> getAllUsers() {
        return userMapper.findAllUsers();
    }

    public UserDto getUserById(int id) {
        return userMapper.findUserById(id);
    }
}
