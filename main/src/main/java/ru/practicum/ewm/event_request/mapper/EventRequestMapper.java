package ru.practicum.ewm.event_request.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event_request.dto.EventRequestDto;
import ru.practicum.ewm.event_request.model.EventRequest;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;

@Mapper(componentModel = "spring", uses = {EventMapper.class, UserMapper.class})
public interface EventRequestMapper {
    EventRequestDto toEventRequestDto(EventRequest eventRequest);

    default Long map(Event event) {
        return event.getId();
    }

    default Long map (User user) {
        return user.getId();
    }
}
