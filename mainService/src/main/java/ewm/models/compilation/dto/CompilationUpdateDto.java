package ewm.models.compilation.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilationUpdateDto {

    @Size(max = 50, message = "Заголовок не может превышать 50 символов")
    private String title;

    private Boolean pinned;
    private List<Long> events;
}
