package ewm.models.event.mapper;

import lombok.experimental.UtilityClass;
import ewm.models.category.mapper.CategoryMapper;
import ewm.models.category.model.Category;
import ewm.models.event.dto.EventRequestDto;
import ewm.models.event.dto.EventResponseDto;
import ewm.models.event.dto.EventShortResponseDto;
import ewm.models.event.model.Event;
import ewm.models.user.mapper.UserMapper;
import ewm.models.user.model.User;

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
                .eventState(event.getEventState())
                .paid(event.getPaid())
                .views(event.getViews())
                .build();
    }
    public Event toEventEntity(EventRequestDto eventRequestDto, Category category, User initiator) {
        return Event.builder()
                .title(eventRequestDto.getTitle())
                .annotation(eventRequestDto.getAnnotation())
                .description(eventRequestDto.getDescription())
                .category(category)
                .initiator(initiator)
                .eventDate(eventRequestDto.getEventDate())
                .paid(eventRequestDto.getPaid())
                .participantLimit(eventRequestDto.getParticipantLimit())
                .requestModeration(eventRequestDto.getRequestModeration())
                .createdOn(eventRequestDto.getCreatedOn())
                .publishedOn(eventRequestDto.getPublishedOn())
                .views(eventRequestDto.getViews())
                .confirmedRequests(eventRequestDto.getConfirmedRequests())
                .eventState(eventRequestDto.getEventState())
                .build();
    }

    public EventShortResponseDto toEventShortResponseDto (Event event) {
        return EventShortResponseDto.builder()
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

}
