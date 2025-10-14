package API.publicAPI.compilations;

import lombok.RequiredArgsConstructor;
import models.apiError.model.NotFoundException;
import models.compilation.dto.CompilationDto;
import models.compilation.mapper.CompilationMapper;
import models.compilation.model.Compilation;
import models.compilation.repo.CompilationRepo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepo compilationRepo;

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int size, int from) {
        Pageable pageable = PageRequest.of(from / size, size);
        return compilationRepo.findByPinnedOptional(pinned, pageable)
                .stream()
                .map(CompilationMapper::toCompilationDto)
                .toList();
    }

    @Override
    public CompilationDto getCompilationById(Long compilationId) {
        Compilation currentCompilation = compilationRepo.findById(compilationId).orElseThrow(() -> new NotFoundException("Подборка не существует"));
        return CompilationMapper.toCompilationDto(currentCompilation);
    }
}
