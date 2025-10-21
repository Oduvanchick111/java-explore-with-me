package ewm.API.privateAPI.participationRequests;

import ewm.models.participationRequest.dto.ParticipationResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ewm.models.participationRequest.dto.EventRequestStatusUpdateRequest;
import ewm.models.participationRequest.dto.EventRequestStatusUpdateResult;
import ewm.models.participationRequest.dto.ParticipationRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class ParticipationRequestController {

    private final ParticipationRequestPrivateService participationRequestPrivateService;

    @GetMapping("/{userId}/events/{eventId}/requests")
    List<ParticipationResponseDto> getRequestsByEventId(@PathVariable Long userId, @PathVariable Long eventId) {
        return participationRequestPrivateService.getRequestsByEventId(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    EventRequestStatusUpdateResult updateRequestStatus(@PathVariable Long userId, @PathVariable Long eventId, @Valid @RequestBody EventRequestStatusUpdateRequest updateRequest) {
        return participationRequestPrivateService.updateRequestStatus(userId, eventId, updateRequest);
    }

    @GetMapping("/{userId}/requests")
    List<ParticipationResponseDto> getRequestByUserId(@PathVariable Long userId) {
        return participationRequestPrivateService.getRequestByUserId(userId);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    ParticipationResponseDto createParticipationRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        return participationRequestPrivateService.createParticipationRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    ParticipationResponseDto cancelRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        return participationRequestPrivateService.cancelRequest(userId, requestId);
    }
}
