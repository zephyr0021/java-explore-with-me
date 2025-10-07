package ru.practicum.ewm.event_request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.common.exception.BadRequestException;
import ru.practicum.ewm.common.exception.ConflictEventRequestException;
import ru.practicum.ewm.common.exception.NotFoundException;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.service.PrivateEventService;
import ru.practicum.ewm.event_request.dto.EventRequestDto;
import ru.practicum.ewm.event_request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.event_request.dto.ListEventRequestDto;
import ru.practicum.ewm.event_request.mapper.EventRequestMapper;
import ru.practicum.ewm.event_request.model.EventRequest;
import ru.practicum.ewm.event_request.model.EventRequestStatus;
import ru.practicum.ewm.event_request.repository.EventRequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public List<EventRequestDto> getEventRequests(Long userId, Long eventId) {
        Event event = privateEventService.getUserEventModel(userId, eventId);

        return eventRequestRepository.findAllByEvent(event)
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

    public ListEventRequestDto updateEventRequestStatus(Long userId, Long eventId,
                                                        EventRequestStatusUpdateRequest request) {
        Event event = privateEventService.getUserEventModel(userId, eventId);
        List<EventRequest> eventRequests = eventRequestRepository.findAllByEventAndIdIn(event, request.getRequestIds());
        ListEventRequestDto result = new ListEventRequestDto();

        boolean isParticipantLimitOver = event.getConfirmedRequests() >= event.getParticipantLimit();
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            return result;
        }
        if (isParticipantLimitOver) {
            throw new ConflictEventRequestException("The participant limit has been reached");
        }

        for (EventRequest eventRequest : eventRequests) {
            if (eventRequest.getStatus() != EventRequestStatus.PENDING) {
                throw new BadRequestException("Request must have status PENDING");
            }
        }

        long newConfirmedRequests = event.getConfirmedRequests();
        List<EventRequestDto> confirmedRequestsDto = new ArrayList<>();
        List<EventRequestDto> rejectedRequestsDto = new ArrayList<>();

        for (EventRequest eventRequest : eventRequests) {
            if (request.getStatus() == EventRequestStatus.CONFIRMED) {
                if (newConfirmedRequests < event.getParticipantLimit()) {
                    eventRequest.setStatus(EventRequestStatus.CONFIRMED);
                    newConfirmedRequests += 1;
                    confirmedRequestsDto.add(eventRequestMapper.toEventRequestDto(eventRequest));
                } else {
                    eventRequest.setStatus(EventRequestStatus.REJECTED);
                    rejectedRequestsDto.add(eventRequestMapper.toEventRequestDto(eventRequest));
                }
            } else if (request.getStatus() == EventRequestStatus.REJECTED) {
                eventRequest.setStatus(EventRequestStatus.REJECTED);
                rejectedRequestsDto.add(eventRequestMapper.toEventRequestDto(eventRequest));
            }
        }

        eventRequestRepository.saveAll(eventRequests);
        result.setConfirmedRequests(confirmedRequestsDto);
        result.setRejectedRequests(rejectedRequestsDto);

        return result;

    }

}
