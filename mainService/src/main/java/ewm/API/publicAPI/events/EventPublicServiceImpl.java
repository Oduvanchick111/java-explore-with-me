package ewm.API.publicAPI.events;

import client.StatsClient;
import dto.ViewStatsDto;
import dto.endpoint.EndpointHitDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ewm.models.apiError.model.NotFoundException;
import ewm.models.event.dto.EventResponseDto;
import ewm.models.event.mapper.EventMapper;
import ewm.models.event.model.Event;
import ewm.models.event.model.EventState;
import ewm.models.event.repo.EventRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventPublicServiceImpl implements EventPublicService {

    private final EventRepo eventRepo;
    private final StatsClient statsClient;
    @Value("${app.name}")
    private String appName;

    @Override
    public List<EventResponseDto> getAllEvents(String text,
                                               List<Long> categories,
                                               Boolean paid,
                                               LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd,
                                               Boolean onlyAvailable,
                                               String sort,
                                               Integer from,
                                               Integer size,
                                               HttpServletRequest request) {
        saveEndpointHit(request);
        Sort sorting = Sort.by("eventDate");
        if ("VIEWS".equalsIgnoreCase(sort)) {
            sorting = Sort.by(Sort.Direction.DESC, "views");
        }

        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, sorting);

        Page<Event> eventPage = eventRepo.findPublicEvents(
                text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                pageable
        );

        if (eventPage.isEmpty()) {
            return Collections.emptyList();
        }

        return eventPage.getContent().stream()
                .map(this::enrichEventWithStats)
                .map(EventMapper::toEventResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventResponseDto getEventById(Long eventId, HttpServletRequest request) {
        Event currentEvent = eventRepo.findById(eventId).orElseThrow(() -> new NotFoundException("Событие не существует"));
        if (!EventState.PUBLISHED.equals(currentEvent.getEventState())) {
            throw new ValidationException("Событие не опубликовано");
        }
        saveEndpointHit(request);
        Event currentEventWithStats = enrichEventWithStats(currentEvent);
        return EventMapper.toEventResponseDto(currentEventWithStats);
    }

    private void saveEndpointHit(HttpServletRequest request) {
        try {
            EndpointHitDto hitDto = new EndpointHitDto();
            hitDto.setApp(appName);
            hitDto.setUri(request.getRequestURI());
            hitDto.setIp(request.getRemoteAddr());
            hitDto.setTimestamp(LocalDateTime.now());
            statsClient.postHit(hitDto);
        } catch (Exception e) {
            log.error("Error saving endpoint hit: {}", e.getMessage());
        }
    }

    private Event enrichEventWithStats(Event event) {
        Long views = getEventViews(event.getId());
        event.setViews(views);
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
