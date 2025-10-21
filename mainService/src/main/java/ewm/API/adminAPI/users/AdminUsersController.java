package ewm.API.adminAPI.users;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ewm.models.user.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUsersController {

    private final AdminUsersService adminUsersService;

    @GetMapping
    List<UserDto> getUsersByAdmin(@RequestParam (required = false) List<Long> ids, @RequestParam(defaultValue = "0") int from, @RequestParam(defaultValue = "10") int size) {
        return adminUsersService.getUsersByAdmin(ids, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserDto createUser(@RequestBody @Valid UserDto userDto) {
        return adminUsersService.createUser(userDto);
    }

    @DeleteMapping("/{userId}")
    void deleteUser(@PathVariable Long userId) {
        adminUsersService.deleteUser(userId);
    }
}
