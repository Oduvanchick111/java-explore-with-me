package ewm.API.publicAPI.users;

import ewm.API.publicAPI.comments.PublicCommentsService;
import ewm.models.comments.dto.CommentResponseDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
@Validated
public class UserController {

    private final PublicCommentsService publicCommentsService;

    @GetMapping("/{userId}/comments")
    public List<CommentResponseDto> getUserComments(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive @Max(100) Integer size) {
        return publicCommentsService.getUserComments(userId, from, size);
    }
}
