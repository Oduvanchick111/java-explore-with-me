package ewm.API.adminAPI.compilations;

import ewm.models.apiError.model.ValidateException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ewm.models.compilation.dto.CompilationCreateDto;
import ewm.models.compilation.dto.CompilationDto;
import ewm.models.compilation.dto.CompilationUpdateDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationsController {

    private final AdminCompilationsService adminCompilationsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CompilationDto createCompilation(@RequestBody @Valid CompilationCreateDto compilationDto) {
        if (compilationDto == null) {
            throw new ValidateException("Тело запроса не может быть пустым");
        }
        return adminCompilationsService.createCompilation(compilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCompilation(@PathVariable Long compId) {
        adminCompilationsService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    CompilationDto updateCompilation(@PathVariable Long compId, @RequestBody @Valid CompilationUpdateDto updateRequest) {
        return adminCompilationsService.updateCompilation(compId, updateRequest);
    }
}
