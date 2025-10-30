package ewm.models.comments.dto;

import ewm.models.user.dto.UserShortDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDto {

    private Long id;

    private Long eventId;

    private UserShortDto user;

    private String text;

    private LocalDateTime created;

    private Boolean edited;

    private Long parentId;

    private List<CommentResponseDto> replies;

}
