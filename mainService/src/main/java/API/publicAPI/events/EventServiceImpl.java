package API.publicAPI.events;

import client.StatsClient;
import dto.ViewStatsDto;
import dto.endpoint.EndpointHitDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import models.apiError.model.NotFoundException;
import models.event.dto.EventResponseDto;
import models.event.mapper.EventMapper;
import models.event.model.Event;
import models.event.model.State;
import models.event.repo.EventRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepo eventRepo;
    private final StatsClient statsClient;

    @Override
    public List<EventResponseDto> getAllEvents(String text,
                                               List<Integer> categories,
                                               Boolean paid,
                                               LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd,
                                               Boolean onlyAvailable,
                                               String sort,
                                               Integer from,
                                               Integer size,
                                               HttpServletRequest request) {
        saveEndpointHit(request);
        List<Event> events = eventRepo.findAll().stream()
                .filter(event -> State.PUBLISHED.equals(event.getState()))
                .filter(event -> filterByText(event, text))
                .filter(event -> filterByCategories(event, categories))
                .filter(event -> filterByPaid(event, paid))
                .filter(event -> filterByDateRange(event, rangeStart, rangeEnd))
                .filter(event -> filterByAvailability(event, onlyAvailable))
                .collect(Collectors.toList());

        events = sortEvents(events, sort);

        events = applyPagination(events, from, size);

        if (events.isEmpty()) {
            return Collections.emptyList();
        }

        return events.stream()
                .map(this::enrichEventWithStats)
                .map(EventMapper::toEventResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventResponseDto getEventById(Long eventId) {
        Event currentEvent = eventRepo.findById(eventId).orElseThrow(() -> new NotFoundException("Событие не существует"));
        return EventMapper.toEventResponseDto(currentEvent);
    }

    private void saveEndpointHit(HttpServletRequest request) {
        try {
            EndpointHitDto hitDto = new EndpointHitDto();
            hitDto.setApp("ewm-main-service");
            hitDto.setUri(request.getRequestURI());
            hitDto.setIp(request.getRemoteAddr());
            hitDto.setTimestamp(LocalDateTime.now());
            statsClient.postHit(hitDto);
        } catch (Exception e) {
            System.err.println("Error saving endpoint hit: " + e.getMessage());
        }
    }

    private boolean filterByText(Event event, String text) {
        if (text == null || text.isBlank()) {
            return true;
        }
        String lowerText = text.toLowerCase();
        return (event.getAnnotation() != null && event.getAnnotation().toLowerCase().contains(lowerText)) ||
                (event.getDescription() != null && event.getDescription().toLowerCase().contains(lowerText));
    }

    private boolean filterByCategories(Event event, List<Integer> categories) {
        if (categories == null || categories.isEmpty()) {
            return true;
        }
        return categories.contains(event.getCategory().getId());
    }

    private boolean filterByPaid(Event event, Boolean paid) {
        if (paid == null) {
            return true;
        }
        return event.isPaid() == (paid);
    }

    private boolean filterByDateRange(Event event, LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        LocalDateTime eventDate = event.getEventDate();
        LocalDateTime start = rangeStart != null ? rangeStart : LocalDateTime.now();

        if (eventDate.isBefore(start)) {
            return false;
        }

        if (rangeEnd != null && eventDate.isAfter(rangeEnd)) {
            return false;
        }

        return true;
    }

    private boolean filterByAvailability(Event event, Boolean onlyAvailable) {
        if (onlyAvailable == null || !onlyAvailable) {
            return true;
        }
        return event.getParticipantLimit() == 0 ||
                event.getConfirmedRequests() < event.getParticipantLimit();
    }

    private List<Event> sortEvents(List<Event> events, String sort) {
        if ("VIEWS".equals(sort)) {
            return events.stream()
                    .sorted(Comparator.comparing(Event::getViews).reversed())
                    .collect(Collectors.toList());
        }
        return events.stream()
                .sorted(Comparator.comparing(Event::getEventDate))
                .collect(Collectors.toList());
    }

    private List<Event> applyPagination(List<Event> events, Integer from, Integer size) {
        int start = Math.min(from, events.size());
        int end = Math.min(start + size, events.size());
        return events.subList(start, end);
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
