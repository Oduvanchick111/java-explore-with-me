package API.privateAPI.events;

import models.event.dto.EventRequestDto;
import models.event.dto.EventResponseDto;

import java.util.List;

public interface EventPrivateService {

    List<EventResponseDto> getEventsByUserId(Long userId, int from, int size);

    EventResponseDto createNewEvent(Long userId, EventRequestDto eventRequestDto);

    EventResponseDto getEventBydEventId(Long userId, Long eventId);

    EventResponseDto updateEventByEventId(Long userId, Long eventId);
}
