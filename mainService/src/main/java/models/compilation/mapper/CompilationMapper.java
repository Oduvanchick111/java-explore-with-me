package models.compilation.mapper;

import lombok.experimental.UtilityClass;
import models.compilation.dto.CompilationDto;
import models.compilation.model.Compilation;
import models.event.dto.EventShortResponseDto;
import models.event.mapper.EventMapper;
import models.event.model.Event;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {

    public Compilation toCompilationEntity(CompilationDto compilationDto, List<Event> events) {
        return Compilation.builder()
                .title(compilationDto.getTitle())
                .pinned(compilationDto.getPinned())
                .events(events)
                .build();
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        List<EventShortResponseDto> eventDtos = Optional.ofNullable(compilation.getEvents())
                .orElse(Collections.emptyList())
                .stream()
                .map(EventMapper::toEventShortResponseDto)
                .collect(Collectors.toList());
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(eventDtos)
                .build();
    }

}
