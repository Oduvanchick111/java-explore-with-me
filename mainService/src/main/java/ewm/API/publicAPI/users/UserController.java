package ewm.API.publicAPI.users;

import ewm.API.publicAPI.comments.PublicCommentsService;
import ewm.models.comments.dto.CommentResponseDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final PublicCommentsService publicCommentsService;

    @GetMapping("/{userId}/comments")
    public List<CommentResponseDto> getUserComments(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer size) {
        return publicCommentsService.getUserComments(userId, from, size);
    }
}
