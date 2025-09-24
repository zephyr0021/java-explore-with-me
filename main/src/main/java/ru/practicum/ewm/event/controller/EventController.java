package ru.practicum.ewm.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.service.EventService;


@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable("eventId") Long eventId, HttpServletRequest request) {
        return eventService.getEvent(eventId, request);
    }

//    @GetMapping
//    public List<EventDto> getEvents(@RequestParam(required = false) String text,
//                                    @RequestParam(required = false) List<Long> categoryIds,
//                                    @RequestParam(required = false) Boolean paid,
//                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//                                        LocalDateTime rangeStart,
//                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//                                        LocalDateTime rangeEnd,
//                                    @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
//                                    @RequestParam(required = false) String sort,
//                                    @RequestParam(defaultValue = "0") Integer from,
//                                    @RequestParam(defaultValue = "10") Integer size
//    ) {
//        return eventService.getEvents(text, categoryIds, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
//    }
}
