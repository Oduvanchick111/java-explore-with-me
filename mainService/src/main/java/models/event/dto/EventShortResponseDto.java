package models.event.dto;

import models.category.dto.CategoryDto;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.user.dto.UserDto;
import models.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventShortResponseDto {

    private Long id;

    @NotBlank
    @Size(max = 200)
    private String title;

    @NotNull
    @Size(max = 1000)
    private String annotation;

    @NotNull
    private CategoryDto category;

    private Long confirmedRequests;

    @NotNull
    @Future
    private LocalDateTime eventDate;

    @NotNull
    private UserShortDto initiator;

    private Boolean paid;

    private Integer views;
}
