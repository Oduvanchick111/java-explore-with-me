package ewm.API.publicAPI.compilations;
import lombok.RequiredArgsConstructor;
import ewm.models.compilation.dto.CompilationDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class CompilationsPublicController {

    private final CompilationPublicService compilationPublicService;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam (required = false) Boolean pinned, @RequestParam (defaultValue = "10") int size, @RequestParam (defaultValue = "0") int from) {
        return compilationPublicService.getCompilations(pinned, size, from);
    }

    @GetMapping("/{compilationId}")
    public CompilationDto getCompilationById(@PathVariable Long compilationId) {
        return compilationPublicService.getCompilationById(compilationId);
    }
}
