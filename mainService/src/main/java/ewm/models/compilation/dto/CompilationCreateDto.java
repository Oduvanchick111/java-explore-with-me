package ewm.models.compilation.dto;

import jakarta.validation.constraints.NotEmpty;
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
public class CompilationCreateDto {
    @NotEmpty(message = "Заголовок не может быть пустым")
    @Size(max = 50, message = "Заголовок не может превышать 50 символов")
    private String title;

    @Builder.Default
    private Boolean pinned = false;

    private List<Long> events;
}
