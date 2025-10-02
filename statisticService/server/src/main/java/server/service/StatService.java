package server.service;

import dto.endpoint.EndpointHitDto;
import dto.ViewStatsDto;
import dto.endpoint.EndpointHitResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {

    EndpointHitResponseDto saveHit(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> getStats(LocalDateTime startDate, LocalDateTime endDate, List<String> uris, Boolean unique);
}
