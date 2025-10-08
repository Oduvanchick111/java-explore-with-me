package API.publicAPI.service;

import lombok.RequiredArgsConstructor;
import models.apiError.model.NotFoundException;
import models.category.dto.CategoryDto;
import models.category.mapper.CategoryMapper;
import models.category.model.Category;
import models.category.repo.CategoryRepo;
import models.compilation.dto.CompilationDto;
import models.compilation.mapper.CompilationMapper;
import models.compilation.model.Compilation;
import models.compilation.repo.CompilationRepo;
import models.event.dto.EventResponseDto;
import models.event.mapper.EventMapper;
import models.event.model.Event;
import models.event.repo.EventRepo;
import models.user.dto.UserDto;
import models.user.dto.UserShortDto;
import models.user.mapper.UserMapper;
import models.user.model.Role;
import models.user.model.User;
import models.user.repo.UserRepo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicServiceImpl implements PublicService {

    private final UserRepo userRepo;
    private final CategoryRepo categoryRepo;
    private final CompilationRepo compilationRepo;
    private final EventRepo eventRepo;

    @Override
    public UserShortDto createUser(UserDto userDto) {
        User currentUser = UserMapper.toUserEntity(userDto);
        userRepo.save(currentUser);
        return UserMapper.toUserShortDto(currentUser);
    }

    @Override
    public UserShortDto createAdmin(UserDto userDto) {
        User currentUser = UserMapper.toUserEntity(userDto);
        currentUser.setRole(Role.ADMIN);
        userRepo.save(currentUser);
        return UserMapper.toUserShortDto(currentUser);
    }

    @Override
    public UserShortDto findUserById(Long id) {
        User currentUser = userRepo.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        return UserMapper.toUserShortDto(currentUser);
    }

    @Override
    public void deleteUserById(Long userId) {
        userRepo.deleteById(userId);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int size, int from) {
        Pageable pageable = PageRequest.of(from / size, size);
        return compilationRepo.findByPinnedOptional(pinned, pageable)
                .stream()
                .map(CompilationMapper::toCompilationDto)
                .toList();
    }

    @Override
    public CompilationDto getCompilationById(Long compilationId) {
        Compilation currentCompilation = compilationRepo.findById(compilationId).orElseThrow(() -> new NotFoundException("Подборка не существует"));
        return CompilationMapper.toCompilationDto(currentCompilation);
    }

    @Override
    public List<CategoryDto> gitCategories(int size, int from) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Category> categories;
        categories = categoryRepo.findAll(pageable).getContent();
        return categories.stream()
                .map(CategoryMapper::toCategoryDto)
                .toList();
    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        Category currentCategory = categoryRepo.findById(categoryId).orElseThrow(() -> new NotFoundException("Категория не существует"));
        return CategoryMapper.toCategoryDto(currentCategory);
    }

    @Override
    public List<EventResponseDto> getAllEvents() {
        return eventRepo.findAll().stream().map(EventMapper::toEventResponseDto).toList();
    }

    @Override
    public EventResponseDto getEventById(Long eventId) {
        Event currentEvent = eventRepo.findById(eventId).orElseThrow(() -> new NotFoundException("Событие не существует"));
        return EventMapper.toEventResponseDto(currentEvent);
    }
}
