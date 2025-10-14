package API.publicAPI.user;

import lombok.RequiredArgsConstructor;
import models.apiError.model.NotFoundException;
import models.user.dto.UserDto;
import models.user.dto.UserShortDto;
import models.user.mapper.UserMapper;
import models.user.model.Role;
import models.user.model.User;
import models.user.repo.UserRepo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    @Override
    public UserShortDto createUser(UserDto userDto) {
        User currentUser = UserMapper.toUserEntity(userDto);
        userRepo.save(currentUser);
        return UserMapper.toUserShortDto(currentUser);
    }

    @Override
    public UserShortDto createAdmin(UserDto userDto) {
        User currentUser = UserMapper.toUserEntity(userDto);
        currentUser.setRole(Role.ADMIN);
        userRepo.save(currentUser);
        return UserMapper.toUserShortDto(currentUser);
    }

    @Override
    public UserShortDto findUserById(Long id) {
        User currentUser = userRepo.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        return UserMapper.toUserShortDto(currentUser);
    }

    @Override
    public void deleteUserById(Long userId) {
        userRepo.deleteById(userId);
    }
}
