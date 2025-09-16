package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.exception.NotFoundException;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event_request.model.EventRequest;
import ru.practicum.ewm.event_request.model.EventRequestStatus;
import ru.practicum.ewm.event_request.repository.EventRequestRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final EventRequestRepository eventRequestRepository;


    public EventFullDto getEvent(Long id) {
        Event event = eventRepository.findEventByIdAndState(id, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Event with id=" + id + " was not found"));
        List<EventRequest> eventRequests = eventRequestRepository
                .findAllByEventIdAndStatus(id, EventRequestStatus.CONFIRMED);

        event.setRequests(eventRequests);

        return eventMapper.toEventFullDto(event);
    }
}
