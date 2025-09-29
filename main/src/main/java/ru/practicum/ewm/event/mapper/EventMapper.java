package ru.practicum.ewm.event.mapper;

import org.mapstruct.*;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventStateAction;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.user.mapper.UserMapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CategoryMapper.class})
public interface EventMapper {

    EventFullDto toEventFullDto(Event event);

    EventDto toEventDto(Event event);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "state", source = "stateAction")
    Event updateEvent(UpdateEventRequest request, @MappingTarget Event event);

    default EventState map(EventStateAction action) {
        if (action == null) {
            return null;
        }

        return switch (action) {
            case PUBLISH_EVENT -> EventState.PUBLISHED;
            case REJECT_EVENT -> EventState.CANCELED;
        };
    }
}
