package ewm.models.user.mapper;

import lombok.experimental.UtilityClass;
import ewm.models.user.dto.UserDto;
import ewm.models.user.dto.UserShortDto;
import ewm.models.user.model.User;

@UtilityClass
public class UserMapper {

    public User toUserEntity(UserDto userDto) {
        return User.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .build();
    }

    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .name(user.getName())
                .build();
    }

    public User toUserEntityFromShortDto(UserShortDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .build();
    }
}
