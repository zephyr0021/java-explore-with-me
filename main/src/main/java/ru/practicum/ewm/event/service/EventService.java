package ru.practicum.ewm.event.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.client.StatClient;
import ru.practicum.ewm.common.exception.NotFoundException;
import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.hit.dto.NewEndpointHitRequest;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final StatClient statClient;

    public EventFullDto getEvent(Long id, HttpServletRequest request) {
        Event event = eventRepository.findEventByIdAndState(id, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Event with id=" + id + " was not found"));
        NewEndpointHitRequest newHit = new NewEndpointHitRequest();

        newHit.setApp("main-app");
        newHit.setIp(request.getRemoteAddr());
        newHit.setUri(request.getRequestURI());
        newHit.setTimestamp(LocalDateTime.now());

        statClient.saveHit(newHit);

        return eventMapper.toEventFullDto(event);
    }

    public List<EventDto> getEvents(String text, List<Long> categoryIds, Boolean paid, LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Integer from,
                                    Integer size) {

    }
}
