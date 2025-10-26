package server.service;

import dto.endpoint.EndpointHitDto;
import dto.ViewStatsDto;
import dto.endpoint.EndpointHitResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import server.mapper.EndpointHitMapper;
import server.model.EndpointHit;
import server.repostitory.StatServerRepo;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StateServiceImpl implements StatService {

    private final StatServerRepo statServerRepo;

    @Override
    @Transactional
    public EndpointHitResponseDto saveHit(EndpointHitDto endpointHitDto) {
        log.info("Сохранение информации о запросе: приложение={}, URI={}, IP={}, время={}",
                endpointHitDto.getApp(), endpointHitDto.getUri(),
                endpointHitDto.getIp(), endpointHitDto.getTimestamp());
        EndpointHit savedHit = statServerRepo.save(EndpointHitMapper.toEndpointHit(endpointHitDto));
        log.info("Запрос сохранен в базе данных, присвоен ID: {}", savedHit.getId());
        return EndpointHitMapper.toEndpointHitResponseDto(savedHit);
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime startDate, LocalDateTime endDate, List<String> uris, Boolean unique) {
        log.info("Формирование статистики: период с {} по {}, URIs={}, уникальные IP={}",
                startDate, endDate, uris, unique);

        if (startDate.isAfter(endDate)) {
            log.error("Некорректный временной интервал: начальная дата {} позже конечной {}", startDate, endDate);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Дата начала не должна быть позже даты окончания");
        }

        List<ViewStatsDto> result;
        if (unique) {
            log.info("Выполнение запроса статистики с учетом уникальных IP-адресов");
            result = statServerRepo.getUniqueStats(startDate, endDate, uris);
        } else {
            log.info("Выполнение запроса статистики всех запросов (без учета уникальности IP)");
            result = statServerRepo.getStats(startDate, endDate, uris);
        }

        log.info("Статистика сформирована: получено {} записей", result != null ? result.size() : 0);
        return result;
    }
}
