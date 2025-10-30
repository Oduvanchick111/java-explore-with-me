package ewm.API.privateAPI.comments;

import ewm.models.comments.dto.CommentCreateDto;
import ewm.models.comments.dto.CommentResponseDto;
import ewm.models.comments.dto.CommentUpdateDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
public class PrivateCommentsController {
    private final PrivateCommentsService privateCommentsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto createComment(
            @PathVariable Long userId,
            @RequestBody @Valid CommentCreateDto commentCreateDto) {
        return privateCommentsService.createComment(commentCreateDto.getEventId(), userId, commentCreateDto);
    }

    @PatchMapping("/{commentId}")
    public CommentResponseDto updateComment(
            @PathVariable Long userId,
            @PathVariable Long commentId,
            @RequestBody @Valid CommentUpdateDto commentUpdateDto) {
        return privateCommentsService.updateComment(userId, commentId, commentUpdateDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(
            @PathVariable Long userId,
            @PathVariable Long commentId) {
        privateCommentsService.deleteComment(userId, commentId);
    }
}
