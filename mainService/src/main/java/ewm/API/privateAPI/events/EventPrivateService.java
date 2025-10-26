package ewm.API.privateAPI.events;

import ewm.models.event.dto.EventResponseDto;
import ewm.models.event.dto.NewEventRequest;
import ewm.models.event.dto.UpdateEventRequest;

import java.util.List;

public interface EventPrivateService {

    List<EventResponseDto> getEventsByUserId(Long userId, int from, int size);

    EventResponseDto createNewEvent(Long userId, NewEventRequest eventRequestDto);

    EventResponseDto getUserEventByEventId(Long userId, Long eventId);

    EventResponseDto updateEventByEventId(Long userId, Long eventId, UpdateEventRequest updateEventRequest);
}
