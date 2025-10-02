package ru.practicum.ewm.event_request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.common.exception.ConflictEventRequestException;
import ru.practicum.ewm.common.exception.NotFoundException;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.service.PrivateEventService;
import ru.practicum.ewm.event_request.dto.EventRequestDto;
import ru.practicum.ewm.event_request.mapper.EventRequestMapper;
import ru.practicum.ewm.event_request.model.EventRequest;
import ru.practicum.ewm.event_request.model.EventRequestStatus;
import ru.practicum.ewm.event_request.repository.EventRequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventRequestService {
    private final EventRequestRepository eventRequestRepository;
    private final EventRequestMapper eventRequestMapper;
    private final UserService userService;
    private final PrivateEventService privateEventService;

    public List<EventRequestDto> getUserEventRequests(Long userId) {
        User user = userService.getUserModel(userId);

        return eventRequestRepository.findAllByRequester(user)
                .stream()
                .map(eventRequestMapper::toEventRequestDto)
                .toList();
    }

    public EventRequestDto createEventRequest(Long userId, Long eventId) {
        User user = userService.getUserModel(userId);
        Event event = privateEventService.getEventModel(eventId);
        Optional<EventRequest> existingEventRequest = eventRequestRepository
                .findByEventIdAndRequesterId(eventId, userId);
        Integer eventRequestsCount = eventRequestRepository.countEventRequestByEvent(event);

        if (existingEventRequest.isPresent() ||
                event.getInitiator().getId().equals(userId) ||
                event.getState() != EventState.PUBLISHED ||
                Objects.equals(event.getParticipantLimit(), eventRequestsCount)
        ) {
            throw new ConflictEventRequestException("Event_request cannot be created");
        }

        EventRequest eventRequest = new EventRequest();

        eventRequest.setRequester(user);
        eventRequest.setEvent(event);
        eventRequest.setCreated(LocalDateTime.now());

        if (!event.getRequestModeration()) {
            eventRequest.setStatus(EventRequestStatus.CONFIRMED);
        } else {
            eventRequest.setStatus(EventRequestStatus.PENDING);
        }

        eventRequest = eventRequestRepository.save(eventRequest);

        return eventRequestMapper.toEventRequestDto(eventRequest);
    }

    public EventRequestDto cancelEventRequest(Long userId, Long requestId) {
        EventRequest eventRequest = eventRequestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Request with id=" + requestId + " was not found"));

        eventRequest.setStatus(EventRequestStatus.PENDING);

        eventRequest = eventRequestRepository.save(eventRequest);

        return eventRequestMapper.toEventRequestDto(eventRequest);
    }

}
