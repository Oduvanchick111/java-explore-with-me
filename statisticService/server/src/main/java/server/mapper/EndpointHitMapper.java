package server.mapper;

import dto.endpoint.EndpointHitDto;
import dto.endpoint.EndpointHitResponseDto;
import lombok.experimental.UtilityClass;
import server.model.EndpointHit;

@UtilityClass
public class EndpointHitMapper {
    public EndpointHit toEndpointHit(EndpointHitDto endPointHitDto) {
        return EndpointHit.builder()
                .app(endPointHitDto.getApp())
                .uri(endPointHitDto.getUri())
                .ip(endPointHitDto.getIp())
                .timestamp(endPointHitDto.getTimestamp())
                .build();
    }

    public EndpointHitDto toEndpointHitDto(EndpointHit endpointHit) {
        return EndpointHitDto.builder()
                .id(endpointHit.getId())
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .ip(endpointHit.getIp())
                .timestamp(endpointHit.getTimestamp())
                .build();
    }

    public EndpointHitResponseDto toEndpointHitResponseDto(EndpointHit endpointHit) {
        return EndpointHitResponseDto.builder()
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .ip(endpointHit.getIp())
                .timestamp(endpointHit.getTimestamp())
                .build();
    }

}
