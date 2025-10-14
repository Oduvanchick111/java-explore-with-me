package models.event.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.event.model.State;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRequestDto {

    private Long id;

    @NotBlank
    @Size(max = 200)
    private String title;

    @NotBlank
    @Size(max = 1000)
    private String annotation;

    @NotNull
    private Long categoryId;

    @NotNull
    @PastOrPresent
    private LocalDateTime createdOn;

    private String description;

    private Long confirmedRequests;

    @NotNull
    @Future
    private LocalDateTime eventDate;

    @NotNull
    private Long initiatorId;

    private Integer participantLimit;

    @FutureOrPresent
    private LocalDateTime publishedOn;

    private boolean requestModeration;

    @NotNull
    private State state;

    private boolean paid;

    private Long views;
}

