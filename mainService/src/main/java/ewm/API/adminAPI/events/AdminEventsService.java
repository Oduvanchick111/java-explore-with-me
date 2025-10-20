package ewm.API.adminAPI.events;

import ewm.models.event.dto.EventResponseDto;
import ewm.models.event.dto.UpdateEventRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventsService {
    List<EventResponseDto> getEventsByAdmin(List<Long> users, List<String> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    EventResponseDto updateEventByAdmin(Long eventId, UpdateEventRequest updateRequest);
}
