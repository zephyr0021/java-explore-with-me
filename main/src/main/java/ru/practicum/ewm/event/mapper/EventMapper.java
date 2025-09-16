package ru.practicum.ewm.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event_request.model.EventRequest;
import ru.practicum.ewm.user.mapper.UserMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CategoryMapper.class})
public interface EventMapper {

    @Mapping(target = "confirmedRequests", source = "requests", qualifiedByName = "countConfirmedRequests")
    EventFullDto toEventFullDto(Event event);

    @Named("countConfirmedRequests")
    default Integer countConfirmedRequests(List<EventRequest> requests) {
        return requests == null ? 0 : requests.size();
    }
}
