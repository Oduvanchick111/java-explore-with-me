package ewm.API.privateAPI.participationRequests;

import ewm.models.participationRequest.dto.ParticipationResponseDto;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import ewm.models.apiError.model.ConflictException;
import ewm.models.apiError.model.NotFoundException;
import ewm.models.event.model.Event;
import ewm.models.event.repo.EventRepo;
import ewm.models.participationRequest.dto.EventRequestStatusUpdateRequest;
import ewm.models.participationRequest.dto.EventRequestStatusUpdateResult;
import ewm.models.participationRequest.dto.ParticipationRequestDto;
import ewm.models.participationRequest.mapper.ParticipationRequestMapper;
import ewm.models.participationRequest.model.ParticipationRequest;
import ewm.models.participationRequest.model.Status;
import ewm.models.participationRequest.repo.ParticipationRepo;
import ewm.models.user.model.User;
import ewm.models.user.repo.UserRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ParticipationRequestPrivateServiceImpl implements ParticipationRequestPrivateService {

    private final UserRepo userRepo;
    private final EventRepo eventRepo;
    private final ParticipationRepo participationRepo;

    @Override
    public List<ParticipationResponseDto> getRequestsByEventId(Long userId, Long eventId) {
        User user = checkUser(userId);
        Event event = eventRepo.findEventByUserIdAndEventId(userId, eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + " не найдено"));

        List<ParticipationRequest> requests = participationRepo.findAllByEventId(eventId);

        return requests.stream().map(ParticipationRequestMapper::toParticipationResponseDto).toList();
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId,
                                                              EventRequestStatusUpdateRequest updateRequest) {
        User user = checkUser(userId);
        Event event = eventRepo.findEventByUserIdAndEventId(userId, eventId).orElseThrow(() -> new NotFoundException("Событие не найдено"));

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            throw new ConflictException("Подтверждение заявок не требуется");
        }

        List<ParticipationRequest> requests = participationRepo.findByIdIn(updateRequest.getRequestIds());

        validateRequests(requests, eventId);

        if (updateRequest.getStatus() == Status.CONFIRMED) {
            return confirmRequests(event, requests);
        } else {
            return rejectRequests(requests);
        }
    }

    @Override
    public List<ParticipationResponseDto> getRequestByUserId(Long userId) {
        User user = checkUser(userId);
        return participationRepo.findByRequesterId(userId).stream().map(ParticipationRequestMapper::toParticipationResponseDto).toList();
    }

    @Override
    @Transactional
    public ParticipationResponseDto createParticipationRequest(Long userId, Long eventId) {
        User user = checkUser(userId);

        if (participationRepo.existsByEventIdAndRequesterId(eventId, userId)) {
            throw new ConflictException("Нельзя добавить повторный запрос");
        }

        Event event = eventRepo.findByIdWithChecks(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено или недоступно"));

        if (event.getParticipantLimit() > 0) {
            long confirmedCount = participationRepo.countByEventIdAndStatus(eventId, Status.CONFIRMED);
            if (confirmedCount >= event.getParticipantLimit()) {
                throw new ConflictException("Достигнут лимит участников");
            }
        }

        ParticipationRequest request = ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(userRepo.getReferenceById(userId))
                .status(event.getRequestModeration() && event.getParticipantLimit() > 0 ?
                        Status.PENDING : Status.CONFIRMED)
                .build();

        ParticipationRequest saved = participationRepo.save(request);

        return ParticipationRequestMapper.toParticipationResponseDto(saved);
    }

    @Override
    @Transactional
    public ParticipationResponseDto cancelRequest(Long userId, Long requestId) {
        User requester = checkUser(userId);

        ParticipationRequest request = participationRepo.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Запрос не найден"));

        if (request.getStatus() != Status.PENDING && request.getStatus() != Status.CONFIRMED) {
            throw new ConflictException("Уже отменен или отклонен");
        }

        request.setStatus(Status.CANCELED);

        ParticipationRequest updated = participationRepo.save(request);

        return ParticipationRequestMapper.toParticipationResponseDto(updated);
    }

    private User checkUser(Long userId) {
        return userRepo.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    private void validateRequests(List<ParticipationRequest> requests, Long eventId) {
        for (ParticipationRequest request : requests) {
            if (request.getStatus() != Status.PENDING) {
                throw new ConflictException("Можно изменить только заявки в состоянии ожидания");
            }
            if (!request.getEvent().getId().equals(eventId)) {
                throw new ConflictException("Заявка не принадлежит событию");
            }
        }
    }

    private EventRequestStatusUpdateResult confirmRequests(Event event, List<ParticipationRequest> requests) {
        Long eventId = event.getId();
        int participantLimit = event.getParticipantLimit();

        long confirmedCount = participationRepo.countByEventIdAndStatus(eventId, Status.CONFIRMED);
        long availableSlots = participantLimit - confirmedCount;

        if (availableSlots <= 0) {
            throw new ConflictException("Достигнут лимит заявок");
        }

        List<ParticipationRequest> confirmed = new ArrayList<>();
        List<ParticipationRequest> rejected = new ArrayList<>();

        for (ParticipationRequest request : requests) {
            if (availableSlots > 0) {
                request.setStatus(Status.CONFIRMED);
                confirmed.add(request);
                availableSlots--;
            } else {
                request.setStatus(Status.REJECTED);
                rejected.add(request);
            }
        }

        if (availableSlots == 0) {
            rejectAllPendingForEvent(eventId);
        }

        return createResult(confirmed, rejected);
    }

    private EventRequestStatusUpdateResult rejectRequests(List<ParticipationRequest> requests) {
        for (ParticipationRequest request : requests) {
            request.setStatus(Status.REJECTED);
        }

        return createResult(List.of(), requests);
    }

    private void rejectAllPendingForEvent(Long eventId) {
        List<ParticipationRequest> pendingRequests = participationRepo.findByEventIdAndStatus(eventId, Status.PENDING);

        for (ParticipationRequest request : pendingRequests) {
            request.setStatus(Status.REJECTED);
        }
    }

    private EventRequestStatusUpdateResult createResult(List<ParticipationRequest> confirmed,
                                                        List<ParticipationRequest> rejected) {
        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmed.stream()
                        .map(ParticipationRequestMapper::toParticipationResponseDto)
                        .collect(Collectors.toList()))
                .rejectedRequests(rejected.stream()
                        .map(ParticipationRequestMapper::toParticipationResponseDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
