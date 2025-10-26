package ewm.API.publicAPI.comments;

import ewm.models.comments.dto.CommentResponseDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Slf4j
public class PublicCommentsController {

    private final PublicCommentsService publicCommentsService;

    @GetMapping("/{commentId}")
    public CommentResponseDto getCommentById(@PathVariable Long commentId) {
        return publicCommentsService.getCommentById(commentId);
    }

    @GetMapping("/{commentId}/replies")
    public List<CommentResponseDto> getReplies(
            @PathVariable Long commentId,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer size) {
        return publicCommentsService.getReplies(commentId, from, size);
    }
}
