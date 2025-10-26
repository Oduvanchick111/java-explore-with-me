package ewm.API.adminAPI.events;

import client.StatsClient;
import dto.ViewStatsDto;
import ewm.models.participationRequest.model.Status;
import ewm.models.participationRequest.repo.ParticipationRepo;
import ewm.models.apiError.model.ConflictException;
import ewm.models.apiError.model.NotFoundException;
import ewm.models.category.model.Category;
import ewm.models.category.repo.CategoryRepo;
import ewm.models.event.dto.EventResponseDto;
import ewm.models.event.dto.UpdateEventRequest;
import ewm.models.event.mapper.EventMapper;
import ewm.models.event.model.Event;
import ewm.models.event.model.EventState;
import ewm.models.event.model.StateAction;
import ewm.models.event.repo.EventRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminEventsServiceImpl implements AdminEventsService {

    private final StatsClient statsClient;
    private final EventRepo eventRepo;
    private final CategoryRepo categoryRepo;
    private final ParticipationRepo participationRepo;

    @Override
    @Transactional(readOnly = true)
    public List<EventResponseDto> getEventsByAdmin(List<Long> users, List<EventState> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        Page<Event> events;

        if (users != null && states != null && categories != null) {
            events = eventRepo.findWithAllFilters(users, states, categories, rangeStart, rangeEnd, pageable);
        } else if (users != null && states != null) {
            events = eventRepo.findWithUsersAndStates(users, states, rangeStart, rangeEnd, pageable);
        } else if (users != null && categories != null) {
            events = eventRepo.findWithUsersAndCategories(users, categories, rangeStart, rangeEnd, pageable);
        } else if (states != null && categories != null) {
            events = eventRepo.findWithStatesAndCategories(states, categories, rangeStart, rangeEnd, pageable);
        } else if (users != null) {
            events = eventRepo.findWithUsers(users, rangeStart, rangeEnd, pageable);
        } else if (states != null) {
            events = eventRepo.findWithStates(states, rangeStart, rangeEnd, pageable);
        } else if (categories != null) {
            events = eventRepo.findWithCategories(categories, rangeStart, rangeEnd, pageable);
        } else {
            events = eventRepo.findWithDateRange(rangeStart, rangeEnd, pageable);
        }

        return events.stream()
                .map(this::enrichEventWithStats)
                .map(EventMapper::toEventResponseDto)
                .toList();
    }

    @Override
    public EventResponseDto updateEventByAdmin(Long eventId, UpdateEventRequest updateRequest) {
        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + " не найдено"));

        if (updateRequest.getAnnotation() != null) {
            event.setAnnotation(updateRequest.getAnnotation());
        }

        if (updateRequest.getCategoryId() != null) {
            Category category = categoryRepo.findById(updateRequest.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Категория не найдена"));
            event.setCategory(category);
        }

        if (updateRequest.getDescription() != null) {
            event.setDescription(updateRequest.getDescription());
        }

        if (updateRequest.getEventDate() != null) {
            event.setEventDate(updateRequest.getEventDate());
        }

        if (updateRequest.getPaid() != null) {
            event.setPaid(updateRequest.getPaid());
        }

        if (updateRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateRequest.getParticipantLimit());
        }

        if (updateRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateRequest.getRequestModeration());
        }

        if (updateRequest.getTitle() != null) {
            event.setTitle(updateRequest.getTitle());
        }

        processAdminStateAction(event, updateRequest.getStateAction());

        Event updatedEvent = eventRepo.save(event);

        return EventMapper.toEventResponseDto(updatedEvent);
    }

    private void processAdminStateAction(Event event, StateAction stateAction) {
        if (stateAction == null) return;

        switch (stateAction) {
            case PUBLISH_EVENT:
                publishEvent(event);
                break;
            case REJECT_EVENT:
                rejectEvent(event);
                break;
            default:
                throw new IllegalArgumentException("Неизвестное действие: " + stateAction);
        }
    }

    private void publishEvent(Event event) {
        if (event.getEventState() != EventState.PENDING) {
            throw new ConflictException("Событие можно публиковать только из состояния ожидания");
        }
        LocalDateTime minEventDate = LocalDateTime.now().plusHours(1);
        if (event.getEventDate().isBefore(minEventDate)) {
            throw new ConflictException("Дата события должна быть не ранее чем за час от публикации");
        }
        event.setEventState(EventState.PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());
    }

    private void rejectEvent(Event event) {
        if (event.getEventState() == EventState.PUBLISHED) {
            throw new ConflictException("Нельзя отклонить опубликованное событие");
        }
        event.setEventState(EventState.CANCELED);
    }

    private Event enrichEventWithStats(Event event) {
        Long views = getEventViews(event.getId());
        Long confirmedRequests = participationRepo.countByEventIdAndStatus(event.getId(), Status.CONFIRMED);
        event.setViews(views);
        event.setConfirmedRequests(confirmedRequests);
        return event;
    }

    private Long getEventViews(Long eventId) {
        try {
            List<ViewStatsDto> stats = statsClient.getStatistics(
                    LocalDateTime.now().minusYears(1),
                    LocalDateTime.now().plusYears(1),
                    List.of("/events/" + eventId),
                    true
            );

            return stats.isEmpty() ? 0L : stats.get(0).getHits();
        } catch (Exception e) {
            return 0L;
        }
    }
}
