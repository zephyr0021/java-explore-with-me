package ru.practicum.ewm.event_request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
