package ewm.API.adminAPI.compilations;

import lombok.AllArgsConstructor;
import ewm.models.apiError.model.ConflictException;
import ewm.models.apiError.model.NotFoundException;
import ewm.models.compilation.dto.CompilationCreateDto;
import ewm.models.compilation.dto.CompilationDto;
import ewm.models.compilation.dto.CompilationUpdateDto;
import ewm.models.compilation.mapper.CompilationMapper;
import ewm.models.compilation.model.Compilation;
import ewm.models.compilation.repo.CompilationRepo;
import ewm.models.event.model.Event;
import ewm.models.event.repo.EventRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminCompilationsServiceImpl implements AdminCompilationsService {
    private final CompilationRepo compilationRepo;
    private final EventRepo eventRepo;

    @Override
    @Transactional
    public CompilationDto createCompilation(CompilationCreateDto compilationDto) {
        if (compilationRepo.existsByTitle(compilationDto.getTitle())) {
            throw new ConflictException("Подборка с заголовком '" + compilationDto.getTitle() + "' уже существует");
        }

        Compilation compilation = Compilation.builder()
                .title(compilationDto.getTitle())
                .pinned(compilationDto.getPinned() != null ? compilationDto.getPinned() : false)
                .build();

        if (compilationDto.getEvents() != null && !compilationDto.getEvents().isEmpty()) {
            List<Long> validEventIds = compilationDto.getEvents().stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (!validEventIds.isEmpty()) {
                List<Event> events = eventRepo.findByIdIn(validEventIds);
                compilation.setEvents(new HashSet<>(events));
            }
        }

        Compilation saved = compilationRepo.save(compilation);
        return CompilationMapper.toCompilationDto(saved);
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        Compilation compilation = compilationRepo.findById(compId).orElseThrow(() -> new NotFoundException("Подборка не найдена"));
        compilationRepo.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long compId, CompilationUpdateDto updateRequest) {
        Compilation compilation = compilationRepo.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка с id=" + compId + " не найдена"));

        if (updateRequest.getTitle() != null) {
            if (!updateRequest.getTitle().equals(compilation.getTitle()) && compilationRepo.existsByTitle(updateRequest.getTitle())) {
                throw new ConflictException("Заголовок уже занят");
            }
            compilation.setTitle(updateRequest.getTitle());
        }

        if (updateRequest.getPinned() != null) {
            compilation.setPinned(updateRequest.getPinned());
        }

        if (updateRequest.getEvents() != null) {
            List<Event> events = eventRepo.findAllById(updateRequest.getEvents());
            compilation.setEvents(new HashSet<>(events));
        }

        Compilation updated = compilationRepo.save(compilation);
        return CompilationMapper.toCompilationDto(updated);
    }
}
