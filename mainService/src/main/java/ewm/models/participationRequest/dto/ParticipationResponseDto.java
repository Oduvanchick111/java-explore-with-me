package ewm.models.participationRequest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ewm.models.participationRequest.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipationResponseDto {
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    private Long eventId;

    private Long requesterId;

    private Status status;
}
