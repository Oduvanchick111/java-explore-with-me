package ewm.API.adminAPI.users;

import lombok.AllArgsConstructor;
import ewm.models.apiError.model.ConflictException;
import ewm.models.apiError.model.NotFoundException;
import ewm.models.user.dto.UserDto;
import ewm.models.user.mapper.UserMapper;
import ewm.models.user.model.User;
import ewm.models.user.repo.UserRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminUsersServiceImpl implements AdminUsersService {

    private final UserRepo userRepo;

    @Override
    public List<UserDto> getUsersByAdmin(List<Long> ids, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);

        Page<User> users;

        if (ids != null && !ids.isEmpty()) {
            users = userRepo.findByIdIn(ids, pageable);
        } else {
            users = userRepo.findAll(pageable);
        }

        return users.stream()
                .map(UserMapper::toUserDto).toList();
    }

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        if (userRepo.existsByEmail(userDto.getEmail())) {
            throw new ConflictException("Пользователь с email '" + userDto.getEmail() + "' уже существует");
        }
        User user = UserMapper.toUserEntity(userDto);
        User savedUser = userRepo.save(user);
        return UserMapper.toUserDto(savedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        userRepo.deleteById(userId);
    }
}
