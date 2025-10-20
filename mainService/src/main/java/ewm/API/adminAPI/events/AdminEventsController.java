package ewm.API.adminAPI.events;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ewm.models.event.dto.EventResponseDto;
import ewm.models.event.dto.UpdateEventRequest;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventsController {

    private final AdminEventsService adminEventsService;

    @GetMapping
    List<EventResponseDto> getEventsByAdmin(@RequestParam(required = false) List<Long> users,
                                            @RequestParam(required = false) List<String> states,
                                            @RequestParam(required = false) List<Long> categories,
                                            @RequestParam(required = false) LocalDateTime rangeStart,
                                            @RequestParam(required = false) LocalDateTime rangeEnd,
                                            @RequestParam(defaultValue = "0") int from,
                                            @RequestParam(defaultValue = "10") int size) {
        return adminEventsService.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    EventResponseDto updateEventByAdmin(@PathVariable Long eventId, @RequestBody @Valid UpdateEventRequest updateRequest) {
        return adminEventsService.updateEventByAdmin(eventId, updateRequest);
    }
}
