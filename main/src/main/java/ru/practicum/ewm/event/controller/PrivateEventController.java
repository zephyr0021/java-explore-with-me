package ru.practicum.ewm.event.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.NewEventRequest;
import ru.practicum.ewm.event.dto.PublicUpdateEventRequest;
import ru.practicum.ewm.event.service.PrivateEventService;
import ru.practicum.ewm.event_request.dto.EventRequestDto;
import ru.practicum.ewm.event_request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.event_request.dto.ListEventRequestDto;
import ru.practicum.ewm.event_request.service.EventRequestService;

import java.util.List;

@RestController
@RequestMapping("users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventController {
    private final PrivateEventService privateEventService;
    private final EventRequestService eventRequestService;

    @GetMapping
    public List<EventDto> getUserEvents(@PathVariable("userId") Long userId,
                                        @RequestParam(defaultValue = "0") Integer from,
                                        @RequestParam(defaultValue = "10") Integer size) {
        return privateEventService.getUserEvents(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable("userId") Long userId, @RequestBody @Valid NewEventRequest request) {
        return privateEventService.createEvent(userId, request);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable("userId") Long userId, @PathVariable("eventId") Long eventId) {
        return privateEventService.getUserEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateUserEvent(@PathVariable("userId") Long userId, @PathVariable("eventId") Long eventId,
                                        @RequestBody @Valid PublicUpdateEventRequest request) {
        return privateEventService.updateUserEvent(userId, eventId, request);
    }

    @GetMapping("/{eventId}/requests")
    public List<EventRequestDto> getUserEventRequests(@PathVariable("userId") Long userId,
                                                      @PathVariable("eventId") Long eventId) {
        return eventRequestService.getEventRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public ListEventRequestDto confirmOrRejectEventRequests(@PathVariable("userId") Long userId,
                                                            @PathVariable("eventId") Long eventId,
                                                            @RequestBody EventRequestStatusUpdateRequest request) {
        return eventRequestService.updateEventRequestStatus(userId, eventId, request);
    }
}
