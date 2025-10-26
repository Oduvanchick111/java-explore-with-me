package ewm.API.publicAPI.comments;

import ewm.models.apiError.model.NotFoundException;
import ewm.models.comments.dto.CommentResponseDto;
import ewm.models.comments.mapper.CommentMapper;
import ewm.models.comments.model.Comment;
import ewm.models.comments.repo.CommentRepo;
import ewm.models.event.repo.EventRepo;
import ewm.models.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublicCommentsServiceImpl implements PublicCommentsService {

    private final CommentRepo commentRepo;
    private final EventRepo eventRepo;
    private final UserRepo userRepo;

    @Override
    public CommentResponseDto getCommentById(Long commentId) {
        log.info("Запрос на получение комментария с id={}", commentId);
        Comment comment = commentRepo.findById(commentId).orElseThrow(() -> new NotFoundException("Комментарий не найден"));
        log.info("Комментарий найден: {}", comment.getId());
        return CommentMapper.toResponseDto(comment);
    }

    @Override
    public List<CommentResponseDto> getCommentsForEvent(Long eventId, Integer from, Integer size) {
        if (!eventRepo.existsById(eventId)) {
            throw new NotFoundException("Событие с id=" + eventId + " не найдено");
        }
        int pageNumber = from / size;
        Pageable pageable = PageRequest.of(pageNumber, size);
        Page<Comment> commentsPage = commentRepo.findByEventId(eventId, pageable);
        return commentsPage.getContent().stream()
                .map(CommentMapper::toResponseDto)
                .toList();
    }


    @Override
    public List<CommentResponseDto> getReplies(Long commentId, Integer from, Integer size) {
        log.info("Запрос ответов для комментария id={}", commentId);
        if (!commentRepo.existsById(commentId)) {
            throw new NotFoundException("Комментарий с id=" + commentId + " не найден");
        }
        int pageNumber = from / size;
        Pageable pageable = PageRequest.of(pageNumber, size);
        Page<Comment> replies = commentRepo.findByParentId(commentId, pageable);
        return replies.stream().map(CommentMapper::toResponseDto).toList();
    }

    @Override
    public List<CommentResponseDto> getUserComments(Long userId, Integer from, Integer size) {
        if (!userRepo.existsById(userId)) {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
        int pageNumber = from / size;
        Pageable pageable = PageRequest.of(pageNumber, size);
        Page<Comment> comments = commentRepo.findByUserId(userId, pageable);
        return comments.stream().map(CommentMapper::toResponseDto).toList();
    }
}
