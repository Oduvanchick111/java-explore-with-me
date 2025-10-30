package ewm.API.privateAPI.comments;

import ewm.models.comments.dto.CommentCreateDto;
import ewm.models.comments.dto.CommentResponseDto;
import ewm.models.comments.dto.CommentUpdateDto;

public interface PrivateCommentsService {

    CommentResponseDto createComment(Long eventId, Long userId, CommentCreateDto commentCreateDto);

    CommentResponseDto updateComment(Long userId, Long commentId, CommentUpdateDto commentUpdateDto);

    void deleteComment(Long userId, Long commentId);
}
