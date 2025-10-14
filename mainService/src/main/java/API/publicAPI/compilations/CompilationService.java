package API.publicAPI.compilations;

import models.compilation.dto.CompilationDto;

import java.util.List;

public interface CompilationService {

    List<CompilationDto> getCompilations(Boolean pinned, int size, int from);

    CompilationDto getCompilationById(Long compilationId);
}
