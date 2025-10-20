package ewm.models.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ewm.models.event.dto.EventShortResponseDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompilationDto {

    private Long id;

    @NotBlank
    @Size(max = 200)
    private String title;

    @Builder.Default
    private Boolean pinned = false;

    @Size(min = 1)
    private List<EventShortResponseDto> events;
}
