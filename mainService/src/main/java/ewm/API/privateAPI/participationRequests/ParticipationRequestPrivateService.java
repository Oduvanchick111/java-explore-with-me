package ewm.API.privateAPI.participationRequests;

import ewm.models.participationRequest.dto.EventRequestStatusUpdateRequest;
import ewm.models.participationRequest.dto.EventRequestStatusUpdateResult;
import ewm.models.participationRequest.dto.ParticipationResponseDto;

import java.util.List;

public interface ParticipationRequestPrivateService {
    List<ParticipationResponseDto> getRequestsByEventId(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest);

    List<ParticipationResponseDto> getRequestByUserId(Long userId);

    ParticipationResponseDto createParticipationRequest(Long userId, Long eventId);

    ParticipationResponseDto cancelRequest(Long userId, Long requestId);
}
