package ewm.API.adminAPI.comments;

import ewm.models.apiError.model.NotFoundException;
import ewm.models.comments.repo.CommentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminCommentsServiceImpl implements AdminCommentsService {

    private final CommentRepo commentRepo;

    @Override
    public void deleteComment(Long commentId) {
        if (!commentRepo.existsById(commentId)) {
            throw new NotFoundException("Комментарий с id=" + commentId + " не найден");
        }
        commentRepo.deleteById(commentId);
    }
}
