package ewm.API.adminAPI.compilations;

import ewm.models.compilation.dto.CompilationCreateDto;
import ewm.models.compilation.dto.CompilationDto;
import ewm.models.compilation.dto.CompilationUpdateDto;

public interface AdminCompilationsService {

    CompilationDto createCompilation(CompilationCreateDto compilationDto);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compId, CompilationUpdateDto updateRequest);
}
