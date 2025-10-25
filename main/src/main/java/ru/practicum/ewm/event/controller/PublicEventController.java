package ru.practicum.ewm.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.service.PublicEventService;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicEventController {
    private final PublicEventService publicEventService;

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable("eventId") Long eventId, HttpServletRequest request) {
        return publicEventService.getEvent(eventId, request);
    }

    @GetMapping
    public List<EventDto> getEvents(HttpServletRequest request,
                                    @RequestParam(required = false) String text,
                                    @RequestParam(required = false, name = "categories") List<Long> categoryIds,
                                    @RequestParam(required = false) Boolean paid,
                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                    LocalDateTime rangeStart,
                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                    LocalDateTime rangeEnd,
                                    @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
                                    @RequestParam(required = false) EventSortType sort,
                                    @RequestParam(defaultValue = "0") Integer from,
                                    @RequestParam(defaultValue = "10") Integer size
    ) {
        return publicEventService.getEvents(request, text, categoryIds, paid, rangeStart, rangeEnd, onlyAvailable,
                sort, from, size);
    }
}
