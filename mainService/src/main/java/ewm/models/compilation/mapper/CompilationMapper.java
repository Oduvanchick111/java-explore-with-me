package ewm.models.compilation.mapper;

import lombok.experimental.UtilityClass;
import ewm.models.compilation.dto.CompilationDto;
import ewm.models.compilation.model.Compilation;
import ewm.models.event.dto.EventShortResponseDto;
import ewm.models.event.mapper.EventMapper;
import ewm.models.event.model.Event;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {

    public Compilation toCompilationEntity(CompilationDto compilationDto, Set<Event> events) {
        return Compilation.builder()
                .title(compilationDto.getTitle())
                .pinned(compilationDto.getPinned())
                .events(events)
                .build();
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        List<EventShortResponseDto> eventDtos = Optional.ofNullable(compilation.getEvents())
                .orElse(Collections.emptySet())
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
