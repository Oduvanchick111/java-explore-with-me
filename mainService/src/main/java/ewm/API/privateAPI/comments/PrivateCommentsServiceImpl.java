package ewm.API.privateAPI.comments;

import ewm.models.apiError.model.ConflictException;
import ewm.models.apiError.model.NotFoundException;
import ewm.models.apiError.model.ValidateException;
import ewm.models.comments.dto.CommentCreateDto;
import ewm.models.comments.dto.CommentResponseDto;
import ewm.models.comments.dto.CommentUpdateDto;
import ewm.models.comments.mapper.CommentMapper;
import ewm.models.comments.model.Comment;
import ewm.models.comments.repo.CommentRepo;
import ewm.models.event.model.Event;
import ewm.models.event.model.EventState;
import ewm.models.event.repo.EventRepo;
import ewm.models.user.model.User;
import ewm.models.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrivateCommentsServiceImpl implements PrivateCommentsService {

    private final EventRepo eventRepo;
    private final CommentRepo commentRepo;
    private final UserRepo userRepo;

    @Override
    public CommentResponseDto createComment(Long eventId, Long userId, CommentCreateDto commentCreateDto) {
        Event event = eventRepo.findById(eventId).orElseThrow(() -> new NotFoundException("Событие с id " + eventId + " не найдено"));
        if (event.getEventState() != EventState.PUBLISHED) {
            throw new ConflictException("Нельзя комментировать неопубликованное событие");
        }
        User user = userRepo.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        Comment comment;
        if (commentCreateDto.getParentId() != null) {
            Comment parent = commentRepo.findById(commentCreateDto.getParentId())
                    .orElseThrow(() -> new NotFoundException("Родительский комментарий не найден"));
            if (!parent.getEvent().getId().equals(eventId)) {
                throw new ConflictException("Родительский комментарий принадлежит другому событию");
            }
            comment = CommentMapper.toCommentEntity(commentCreateDto, event, user, parent);
        } else {
            comment = CommentMapper.toCommentEntity(commentCreateDto, event, user);
        }
        Comment savedComment = commentRepo.save(comment);
        log.info("Комментарий создан с id={}", savedComment.getId());
        return CommentMapper.toResponseDto(savedComment);
    }

    @Override
    public CommentResponseDto updateComment(Long userId, Long commentId, CommentUpdateDto commentUpdateDto) {
        if (!userRepo.existsById(userId)) {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
        Comment comment = commentRepo.findById(commentId).orElseThrow(() -> new NotFoundException("Комментарий с id +" + commentId + " не найден"));
        if (!comment.getUser().getId().equals(userId)) {
            throw new ValidateException("Нельзя изменить чужой комментарий");
        }
        comment.setText(commentUpdateDto.getText());
        comment.setUpdated(LocalDateTime.now());
        Comment updatedComment = commentRepo.save(comment);
        return CommentMapper.toResponseDto(updatedComment);
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepo.findById(commentId).orElseThrow(() -> new NotFoundException("Комментарий с id +" + commentId + " не найден"));
        if (!comment.getUser().getId().equals(userId)) {
            throw new ValidateException("Нельзя удалить чужой комментарий");
        }
        commentRepo.deleteById(commentId);
    }
}
