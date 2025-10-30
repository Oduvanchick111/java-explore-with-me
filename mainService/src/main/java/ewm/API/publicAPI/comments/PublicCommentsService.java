package ewm.API.publicAPI.comments;

import ewm.models.comments.dto.CommentResponseDto;

import java.util.List;

public interface PublicCommentsService {

    CommentResponseDto getCommentById(Long commentId);

    List<CommentResponseDto> getCommentsForEvent(Long eventId, Integer from, Integer size);

    List<CommentResponseDto> getReplies(Long commentId, Integer from, Integer size);

    public List<CommentResponseDto> getUserComments(Long userId, Integer from, Integer size);
}
