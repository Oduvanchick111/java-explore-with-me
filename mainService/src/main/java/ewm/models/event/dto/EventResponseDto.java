package ewm.models.event.dto;

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

    @NotBlank
    @Size(max = 200)
    private String title;

    @NotBlank
    @Size(max = 1000)
    private String annotation;

    @NotNull
    private CategoryDto category;

    @NotNull
    @PastOrPresent
    private LocalDateTime createdOn;

    private String description;

    private Integer confirmedRequests;

    @NotNull
    @Future
    private LocalDateTime eventDate;

    @NotNull
    private UserShortDto initiator;

    private Integer participantLimit;

    @FutureOrPresent
    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    @NotNull
    private EventState eventState;

    private Boolean paid;

    private Long views;
}
