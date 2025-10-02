package ru.practicum.ewm.event_request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event_request.dto.EventRequestDto;
import ru.practicum.ewm.event_request.service.EventRequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class EventRequestController {
    private final EventRequestService eventRequestService;

    @GetMapping
    public List<EventRequestDto> getUserEventRequests(@PathVariable Long userId) {
        return eventRequestService.getUserEventRequests(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventRequestDto createUserEventRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        return eventRequestService.createEventRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public EventRequestDto cancelEventRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        return eventRequestService.cancelEventRequest(userId, requestId);
    }
}
