package ewm.API.privateAPI.participationRequests;

import ewm.models.participationRequest.dto.EventRequestStatusUpdateRequest;
import ewm.models.participationRequest.dto.EventRequestStatusUpdateResult;
import ewm.models.participationRequest.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestPrivateService {
    List<ParticipationRequestDto> getRequestsByEventId(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest);

    List<ParticipationRequestDto> getRequestByUserId(Long userId);

    ParticipationRequestDto createParticipationRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}
