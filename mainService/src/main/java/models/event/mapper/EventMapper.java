package models.event.mapper;

import lombok.experimental.UtilityClass;
import models.category.mapper.CategoryMapper;
import models.category.model.Category;
import models.event.dto.EventRequestDto;
import models.event.dto.EventResponseDto;
import models.event.dto.EventShortResponseDto;
import models.event.model.Event;
import models.user.mapper.UserMapper;
import models.user.model.User;

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
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .paid(event.isPaid())
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
                .paid(eventRequestDto.isPaid())
                .participantLimit(eventRequestDto.getParticipantLimit())
                .requestModeration(eventRequestDto.isRequestModeration())
                .createdOn(eventRequestDto.getCreatedOn())
                .publishedOn(eventRequestDto.getPublishedOn())
                .views(eventRequestDto.getViews())
                .confirmedRequests(eventRequestDto.getConfirmedRequests())
                .state(eventRequestDto.getState())
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
                .paid(event.isPaid())
                .views(event.getViews())
                .build();

    }

}
