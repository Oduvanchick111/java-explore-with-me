package ewm.API.privateAPI.events;

import ewm.models.event.dto.NewEventRequest;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import ewm.models.apiError.model.ConflictException;
import ewm.models.apiError.model.NotFoundException;
import ewm.models.category.model.Category;
import ewm.models.category.repo.CategoryRepo;
import ewm.models.event.dto.EventResponseDto;
import ewm.models.event.dto.UpdateEventRequest;
import ewm.models.event.mapper.EventMapper;
import ewm.models.event.model.Event;
import ewm.models.event.model.EventState;
import ewm.models.event.model.StateAction;
import ewm.models.event.repo.EventRepo;
import ewm.models.user.model.User;
import ewm.models.user.repo.UserRepo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class EventPrivateServiceImpl implements EventPrivateService {

    private final UserRepo userRepo;
    private final EventRepo eventRepo;
    private final CategoryRepo categoryRepo;

    @Override
    public List<EventResponseDto> getEventsByUserId(Long userId, int from, int size) {
        User currentUser = userRepo.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        return eventRepo.findByInitiatorId(userId, pageable).stream().map(EventMapper::toEventResponseDto).toList();
    }

    @Override
    @Transactional
    public EventResponseDto createNewEvent(Long userId, NewEventRequest eventRequestDto) {
        User currentUser = userRepo.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Category categoryForCurrentEvent = categoryRepo.findById(eventRequestDto.getCategory()).orElseThrow(() -> new NotFoundException("Такой категории не существует"));
        Event currentEvent = EventMapper.toEvent(eventRequestDto, currentUser, categoryForCurrentEvent);
        if (!checkDate(currentEvent.getEventDate())) {
            throw new ValidationException("Дата проведения события должна быть не ранее, чем за два часа до текущего момента");
        }
        return EventMapper.toEventResponseDto(currentEvent);
    }

    @Override
    public EventResponseDto getUserEventByEventId(Long userId, Long eventId) {
        User currentUser = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не найден"));
        Event event = eventRepo.findEventByUserIdAndEventId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + " не найдено"));
        return EventMapper.toEventResponseDto(event);
    }

    @Override
    @Transactional
    public EventResponseDto updateEventByEventId(Long userId, Long eventId, UpdateEventRequest updateEventRequest) {
        User currentUser = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не найден"));

        Event event = eventRepo.findEventByUserIdAndEventId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + " не найдено"));


        validateEventForUpdate(event, updateEventRequest);

        if (updateEventRequest.getAnnotation() != null && !updateEventRequest.getAnnotation().isBlank()) {
            event.setAnnotation(updateEventRequest.getAnnotation());
        }

        if (updateEventRequest.getCategoryId() != null) {
            Category category = categoryRepo.findById(updateEventRequest.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Категория с id=" + updateEventRequest.getCategoryId() + " не найдена"));
            event.setCategory(category);
        }

        if (updateEventRequest.getDescription() != null && !updateEventRequest.getDescription().isBlank()) {
            event.setDescription(updateEventRequest.getDescription());
        }

        if (updateEventRequest.getEventDate() != null) {
            event.setEventDate(updateEventRequest.getEventDate());
        }

        if (updateEventRequest.getPaid() != null) {
            event.setPaid(updateEventRequest.getPaid());
        }

        if (updateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }

        if (updateEventRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventRequest.getRequestModeration());
        }

        if (updateEventRequest.getTitle() != null && !updateEventRequest.getTitle().isBlank()) {
            event.setTitle(updateEventRequest.getTitle());
        }

        processStateAction(event, updateEventRequest.getStateAction());

        Event updatedEvent = eventRepo.save(event);

        return EventMapper.toEventResponseDto(updatedEvent);
    }

    private Boolean checkDate(LocalDateTime eventDate) {
        return eventDate.isBefore(LocalDateTime.now().plusHours(2));
    }

    private void validateEventForUpdate(Event event, UpdateEventRequest updateRequest) {
        if (event.getEventState() == EventState.PUBLISHED) {
            throw new ConflictException("Нельзя редактировать опубликованное событие");
        }

        if (updateRequest.getParticipantLimit() != null && updateRequest.getParticipantLimit() < 0) {
            throw new ValidationException("Лимит участников не может быть отрицательным");
        }

        if (!checkDate(updateRequest.getEventDate())) {
            throw new ConflictException("Дата проведения события должна быть не ранее, чем за два часа до текущего момента");
        }
    }

    private void processStateAction(Event event, StateAction stateAction) {
        if (stateAction == null) {
            return;
        }
        switch (stateAction) {
            case SEND_TO_REVIEW:
                if (event.getEventState() != EventState.CANCELED) {
                    throw new ConflictException("Можно отправить на модерацию только отмененное событие");
                }
                event.setEventState(EventState.PENDING);
                break;

            case CANCEL_REVIEW:
                if (event.getEventState() != EventState.PENDING) {
                    throw new ConflictException("Можно отменить только событие в состоянии модерации");
                }
                event.setEventState(EventState.CANCELED);
                break;

            case PUBLISH_EVENT:
            case REJECT_EVENT:
                throw new ConflictException("Пользователь не может публиковать или отклонять события");

            default:
                throw new IllegalArgumentException("Неизвестное действие: " + stateAction);
        }
    }
}
