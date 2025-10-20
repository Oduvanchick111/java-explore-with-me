package ewm.models.participationRequest.mapper;

import lombok.experimental.UtilityClass;
import ewm.models.event.model.Event;
import ewm.models.participationRequest.dto.ParticipationRequestDto;
import ewm.models.participationRequest.model.ParticipationRequest;
import ewm.models.user.model.User;

@UtilityClass
public class ParticipationRequestMapper {

    public ParticipationRequest toParticipationRequestEntity (ParticipationRequestDto participationRequestDto, User requester, Event event) {
        return ParticipationRequest.builder()
                .created(participationRequestDto.getCreated())
                .status(participationRequestDto.getStatus())
                .requester(requester)
                .event(event)
                .build();
    }

    public ParticipationRequestDto toParticipationRequestDto (ParticipationRequest participationRequest) {
        return ParticipationRequestDto.builder()
                .id(participationRequest.getId())
                .requesterId(participationRequest.getRequester().getId())
                .eventId(participationRequest.getEvent().getId())
                .status(participationRequest.getStatus()).build();
    }
}
