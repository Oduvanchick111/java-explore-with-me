package ewm.API.publicAPI.events;

import jakarta.servlet.http.HttpServletRequest;
import ewm.models.event.dto.EventResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventPublicService {

    List<EventResponseDto> getAllEvents(String text,
                                        List<Long> categories,
                                        Boolean paid,
                                        LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd,
                                        Boolean onlyAvailable,
                                        String sort,
                                        Integer from,
                                        Integer size,
                                        HttpServletRequest request);

    EventResponseDto getEventById(Long eventId, HttpServletRequest request);
}
