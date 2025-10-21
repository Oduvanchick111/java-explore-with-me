package ewm.models.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ewm.models.category.dto.CategoryDto;
import ewm.models.event.model.EventState;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ewm.models.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResponseDto {

    private Long id;

    private String title;

    private String annotation;

    private CategoryDto category;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    private String description;

    private Integer confirmedRequests;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    
    private UserShortDto initiator;

    private Integer participantLimit;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    private EventState state;

    private Boolean paid;

    private Long views;
}
