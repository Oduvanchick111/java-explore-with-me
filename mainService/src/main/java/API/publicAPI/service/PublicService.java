package API.publicAPI.service;

import models.category.dto.CategoryDto;
import models.compilation.dto.CompilationDto;
import models.event.dto.EventRequestDto;
import models.event.dto.EventResponseDto;
import models.user.dto.UserDto;
import models.user.dto.UserShortDto;

import java.util.List;

public interface PublicService {
    UserShortDto createUser(UserDto userDto);

    UserShortDto createAdmin(UserDto userDto);

    UserShortDto findUserById(Long id);

    void deleteUserById(Long userId);

    List<CompilationDto> getCompilations(Boolean pinned, int size, int from);

    CompilationDto getCompilationById(Long compilationId);

    List<CategoryDto> gitCategories(int size, int from);

    CategoryDto getCategoryById(Long categoryId);

    List<EventResponseDto> getAllEvents();

    EventResponseDto getEventById(Long eventId);


}
