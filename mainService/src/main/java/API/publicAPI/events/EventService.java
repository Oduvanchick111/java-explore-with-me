package API.publicAPI.events;

import jakarta.servlet.http.HttpServletRequest;
import models.event.dto.EventResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    List<EventResponseDto> getAllEvents(String text,
                                        List<Integer> categories,
                                        Boolean paid,
                                        LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd,
                                        Boolean onlyAvailable,
                                        String sort,
                                        Integer from,
                                        Integer size,
                                        HttpServletRequest request);

    EventResponseDto getEventById(Long eventId);
}
