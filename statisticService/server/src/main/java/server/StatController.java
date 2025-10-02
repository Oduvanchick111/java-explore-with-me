package server;

import dto.endpoint.EndpointHitDto;
import dto.ViewStatsDto;
import dto.endpoint.EndpointHitResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import server.service.StatService;

import java.time.LocalDateTime;
import java.util.List;

import static dto.ForDate.DATE_TIME_FORMAT;

@RestController
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("unused")
public class StatController {
    private final StatService statService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitResponseDto saveHit(@RequestBody EndpointHitDto endpointHitDto) {
        log.info("Controller: Запрос на сохранение нового хита.");
        log.debug("Сохраняемый хит: {}", endpointHitDto);
        return statService.saveHit(endpointHitDto);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<ViewStatsDto> getStats(@RequestParam(name = "start", required = true)
                                       @DateTimeFormat(pattern = DATE_TIME_FORMAT)
                                       LocalDateTime start,
                                       @RequestParam(name = "end", required = true)
                                       @DateTimeFormat(pattern = DATE_TIME_FORMAT)
                                       LocalDateTime end,
                                       @RequestParam(name = "uris", required = false) List<String> uris,
                                       @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {

        log.info("Controller: Запрос на получение статистики получен.");
        log.debug("Параметры запроса: start={}, end={}, uris={}, unique={}",
                start, end, uris, unique);

        return statService.getStats(start, end, (uris == null || uris.isEmpty() ? null : uris), unique);
    }
}
