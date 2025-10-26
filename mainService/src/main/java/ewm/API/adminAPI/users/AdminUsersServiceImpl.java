package ewm.API.adminAPI.users;

import ewm.models.apiError.model.ConflictException;
import ewm.models.apiError.model.NotFoundException;
import ewm.models.user.dto.UserDto;
import ewm.models.user.mapper.UserMapper;
import ewm.models.user.model.User;
import ewm.models.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminUsersServiceImpl implements AdminUsersService {

    private final UserRepo userRepo;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsersByAdmin(List<Long> ids, int from, int size) {

        int page = from / size;

        Pageable pageable = PageRequest.of(page, size);

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
    public UserDto createUser(UserDto userDto) {
        if (userRepo.existsByEmail(userDto.getEmail())) {
            throw new ConflictException("Пользователь с email '" + userDto.getEmail() + "' уже существует");
        }
        User user = UserMapper.toUserEntity(userDto);
        User savedUser = userRepo.save(user);
        return UserMapper.toUserDto(savedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        userRepo.deleteById(userId);
    }
}
