package ewm.API.publicAPI.events;

import ewm.API.publicAPI.comments.PublicCommentsService;
import ewm.models.comments.dto.CommentResponseDto;
import ewm.models.event.dto.EventShortResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import ewm.models.event.dto.EventResponseDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventPublicController {

    private final EventPublicService eventPublicService;
    private final PublicCommentsService publicCommentsService;

    @GetMapping
    public List<EventShortResponseDto> getAllEvents(@RequestParam(required = false) String text,
                                                    @RequestParam(required = false) List<Long> categories,
                                                    @RequestParam(required = false) Boolean paid,
                                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                    @RequestParam(required = false) Boolean onlyAvailable,
                                                    @RequestParam(required = false) String sort,
                                                    @RequestParam(defaultValue = "0") Integer from,
                                                    @RequestParam(defaultValue = "10") Integer size,
                                                    HttpServletRequest request) {
        return eventPublicService.getAllEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/{eventId}")
    public EventResponseDto getEventById(@PathVariable Long eventId, HttpServletRequest request) {
        return eventPublicService.getEventById(eventId, request);
    }

    @GetMapping("/{eventId}/comments")
    public List<CommentResponseDto> getCommentsForEvent(
            @PathVariable Long eventId,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer size) {
        return publicCommentsService.getCommentsForEvent(eventId, from, size);
    }
}
