package ru.practicum.ewm.event.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventShort;
import ru.practicum.ewm.user.mapper.UserMapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CategoryMapper.class})
public interface EventMapper {

    EventFullDto toEventFullDto(Event event);

    EventDto toEventDto(EventShort eventShort);

    default EventFullDto toEventFullDto(Event event, Long confirmedRequests) {
        EventFullDto dto = toEventFullDto(event);
        dto.setConfirmedRequests(confirmedRequests);
        return dto;
    }

    default EventDto toEventDto(EventShort eventShort, Long confirmedRequests) {
        EventDto dto = toEventDto(eventShort);
        dto.setConfirmedRequests(confirmedRequests);
        return dto;
    }
}
