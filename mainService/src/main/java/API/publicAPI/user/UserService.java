package API.publicAPI.user;

import models.user.dto.UserDto;
import models.user.dto.UserShortDto;

public interface UserService {

    UserShortDto createUser(UserDto userDto);

    UserShortDto createAdmin(UserDto userDto);

    UserShortDto findUserById(Long id);

    void deleteUserById(Long userId);
}
