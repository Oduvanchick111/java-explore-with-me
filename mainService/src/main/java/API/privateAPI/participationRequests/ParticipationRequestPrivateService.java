package API.privateAPI.participationRequests;

import models.participationRequest.dto.ParticipationRequestDto;

public interface ParticipationRequestPrivateService {
    ParticipationRequestDto getRequestsByEventId(Long userId, Long eventId);

    ParticipationRequestDto updateRequestByEventId(Long userId, Long eventId, ParticipationRequestDto participationRequestDto);
}
