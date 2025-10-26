package ewm.models.comments.mapper;

import ewm.models.comments.dto.CommentCreateDto;
import ewm.models.comments.dto.CommentResponseDto;
import ewm.models.comments.model.Comment;
import ewm.models.event.model.Event;
import ewm.models.user.mapper.UserMapper;
import ewm.models.user.model.User;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

@UtilityClass
public class CommentMapper {

    public Comment toCommentEntity(CommentCreateDto commentCreateDto, Event event, User user) {
        return Comment.builder()
                .event(event)
                .user(user)
                .text(commentCreateDto.getText())
                .created(LocalDateTime.now())
                .build();
    }

    public Comment toCommentEntity(CommentCreateDto dto, Event event, User user, Comment parent) {
        return Comment.builder()
                .event(event)
                .user(user)
                .text(dto.getText())
                .created(LocalDateTime.now())
                .parent(parent)
                .build();
    }

    public CommentResponseDto toResponseDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .eventId(comment.getEvent().getId())
                .user(UserMapper.toUserShortDto(comment.getUser()))
                .text(comment.getText())
                .created(comment.getCreated())
                .edited(comment.getUpdated() != null)
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .replies(new ArrayList<>())
                .build();
    }

    public CommentResponseDto toResponseDtoWithReplies(Comment comment) {
        CommentResponseDto dto = toResponseDto(comment);
        dto.setReplies(comment.getReplies().stream()
                .map(CommentMapper::toResponseDtoWithReplies)
                .collect(Collectors.toList()));
        return dto;
    }
}
