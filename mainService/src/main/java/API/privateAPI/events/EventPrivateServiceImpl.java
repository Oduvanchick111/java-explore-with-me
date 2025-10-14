package API.privateAPI.events;

import models.event.dto.EventRequestDto;
import models.event.dto.EventResponseDto;

import java.util.List;

public class EventPrivateServiceImpl implements EventPrivateService{
    @Override
    public List<EventResponseDto> getEventsByUserId(Long userId, int from, int size) {
        return List.of();
    }

    @Override
    public EventResponseDto createNewEvent(Long userId, EventRequestDto eventRequestDto) {
        return null;
    }

    @Override
    public EventResponseDto getEventBydEventId(Long userId, Long eventId) {
        return null;
    }

    @Override
    public EventResponseDto updateEventByEventId(Long userId, Long eventId) {
        return null;
    }
}
