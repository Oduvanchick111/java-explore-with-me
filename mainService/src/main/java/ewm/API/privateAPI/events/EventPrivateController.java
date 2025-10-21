package ewm.API.privateAPI.events;

import ewm.models.event.dto.NewEventRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ewm.models.event.dto.EventResponseDto;
import ewm.models.event.dto.UpdateEventRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class EventPrivateController {

    private final EventPrivateService eventPrivateService;

    @GetMapping("/{userId}/events")
    List<EventResponseDto> getEventsByUserId(@PathVariable Long userId, @RequestParam(defaultValue = "0") int from, @RequestParam(defaultValue = "10") int size) {
        return eventPrivateService.getEventsByUserId(userId, from, size);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    EventResponseDto createNewEvent(@PathVariable Long userId, @Valid @RequestBody NewEventRequest eventRequestDto) {
        return eventPrivateService.createNewEvent(userId, eventRequestDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    EventResponseDto getUserEventByEventId(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventPrivateService.getUserEventByEventId(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    EventResponseDto updateEventByEventId(@PathVariable Long userId, @PathVariable Long eventId, @Valid @RequestBody UpdateEventRequest updateEventRequest) {
        return eventPrivateService.updateEventByEventId(userId, eventId, updateEventRequest);
    }
}
