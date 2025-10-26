package ewm.API.publicAPI.compilations;

import ewm.models.compilation.dto.CompilationDto;

import java.util.List;

public interface CompilationPublicService {

    List<CompilationDto> getCompilations(Boolean pinned, int size, int from);

    CompilationDto getCompilationById(Long compilationId);
}
