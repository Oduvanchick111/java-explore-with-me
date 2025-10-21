package ewm.models.event.mapper;

import ewm.models.event.dto.NewEventRequest;
import ewm.models.event.model.EventState;
import lombok.experimental.UtilityClass;
import ewm.models.category.mapper.CategoryMapper;
import ewm.models.category.model.Category;
import ewm.models.event.dto.EventResponseDto;
import ewm.models.event.dto.EventShortResponseDto;
import ewm.models.event.model.Event;
import ewm.models.user.mapper.UserMapper;
import ewm.models.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class EventMapper {

    public EventResponseDto toEventResponseDto(Event event) {
        return EventResponseDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .createdOn(event.getCreatedOn())
                .confirmedRequests(event.getConfirmedRequests() != null ? event.getConfirmedRequests().intValue() : 0)
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getEventState())
                .paid(event.getPaid())
                .views(event.getViews())
                .location(event.getLocation())
                .build();
    }


    public EventShortResponseDto toEventShortResponseDto(Event event) {
        return EventShortResponseDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .views(event.getViews())
                .build();

    }

    public static Event toEvent(NewEventRequest newEventRequest, User initiator, Category category) {
        return Event.builder()
                .title(newEventRequest.getTitle())
                .annotation(newEventRequest.getAnnotation())
                .description(newEventRequest.getDescription())
                .category(category)
                .initiator(initiator)
                .eventDate(newEventRequest.getEventDate())
                .paid(newEventRequest.getPaid() != null ? newEventRequest.getPaid() : false)
                .participantLimit(newEventRequest.getParticipantLimit() != null ? newEventRequest.getParticipantLimit() : 0)
                .requestModeration(newEventRequest.getRequestModeration() != null ? newEventRequest.getRequestModeration() : true)
                .createdOn(LocalDateTime.now())
                .eventState(EventState.PENDING)
                .location(newEventRequest.getLocation())
                .views(0L)
                .confirmedRequests(0L)
                .build();
    }
}
