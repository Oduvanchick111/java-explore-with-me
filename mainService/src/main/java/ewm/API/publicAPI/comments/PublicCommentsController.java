package ewm.API.publicAPI.comments;

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
@RequestMapping("/comments")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PublicCommentsController {

    private final PublicCommentsService publicCommentsService;

    @GetMapping("/{commentId}")
    public CommentResponseDto getCommentById(@PathVariable Long commentId) {
        return publicCommentsService.getCommentById(commentId);
    }

    @GetMapping("/{commentId}/replies")
    public List<CommentResponseDto> getReplies(
            @PathVariable Long commentId,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive @Max(100) Integer size) {
        return publicCommentsService.getReplies(commentId, from, size);
    }
}
