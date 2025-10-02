package client;

import org.springframework.http.ResponseEntity;
import dto.endpoint.EndpointHitDto;
import dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ClientBase {

    ResponseEntity<EndpointHitDto> postHit(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> getStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
