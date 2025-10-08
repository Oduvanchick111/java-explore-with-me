package models.participationRequest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.participationRequest.model.Status;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipationRequestDto {

    private Long id;

    @PastOrPresent
    private LocalDateTime created;

    @NotNull
    private Long eventId;

    @NotNull
    private Long requesterId;

    @NotNull
    private Status status;

}
