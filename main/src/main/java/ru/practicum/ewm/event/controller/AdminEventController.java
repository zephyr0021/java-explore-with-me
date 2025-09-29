package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.service.AdminEventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("admin/events")
@RequiredArgsConstructor
public class AdminEventController {
    private final AdminEventService adminEventService;

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam(required = false, name = "users") List<Long> usersIds,
                                        @RequestParam(required = false) List<EventState> states,
                                        @RequestParam(required = false, name = "categories") List<Long> categoriesIds,
                                        @RequestParam(required = false) LocalDateTime rangeStart,
                                        @RequestParam(required = false) LocalDateTime rangeEnd,
                                        @RequestParam(defaultValue = "0") Integer from,
                                        @RequestParam(defaultValue = "10") Integer size) {
        return adminEventService.getEvents(usersIds, states, categoriesIds, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable("eventId") Long eventId, @RequestBody UpdateEventRequest request) {
        return adminEventService.updateEvent(eventId, request);
    }
}
