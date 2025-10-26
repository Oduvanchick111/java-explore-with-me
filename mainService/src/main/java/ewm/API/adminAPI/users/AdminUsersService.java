package ewm.API.adminAPI.users;

import ewm.models.user.dto.UserDto;

import java.util.List;

public interface AdminUsersService {

    List<UserDto> getUsersByAdmin(List<Long> ids, int from, int size);

    UserDto createUser(UserDto userDto);

    void deleteUser(Long userId);
}
