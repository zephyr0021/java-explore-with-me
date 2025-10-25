package ru.practicum.ewm.event.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.client.StatClient;
import ru.practicum.ewm.common.custom.OffsetBasedPageRequest;
import ru.practicum.ewm.common.exception.BadRequestException;
import ru.practicum.ewm.common.exception.NotFoundException;
import ru.practicum.ewm.common.hit.HitCreator;
import ru.practicum.ewm.event.controller.EventSortType;
import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.repository.EventSpecifications;
import ru.practicum.ewm.event_view.service.EventViewService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicEventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final StatClient statClient;
    private final EventViewService eventViewsService;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public EventFullDto getEvent(Long id, HttpServletRequest request) {
        Event event = eventRepository.findEventByIdAndState(id, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Event with id=" + id + " was not found"));

        eventViewsService.addViewToEvent(id, request.getRemoteAddr());
        statClient.saveHit(HitCreator.createHit(request, LocalDateTime.now()));
        entityManager.refresh(event);

        return eventMapper.toEventFullDto(event);
    }

    public List<EventDto> getEvents(HttpServletRequest request, String text, List<Long> categoryIds, Boolean paid,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                    EventSortType sort, Integer from, Integer size) {
        if ((rangeStart != null && rangeEnd != null) && rangeEnd.isBefore(rangeStart)) {
            throw new BadRequestException("rangeEnd must be after rangeStart");
        }

        Sort sorting = Sort.by("id");

        if (sort != null) {
            switch (sort) {
                case EVENT_DATE -> sorting = Sort.by("eventDate").descending();
                case VIEWS -> sorting = Sort.by("views").descending();
            }
        }

        Pageable pageable = new OffsetBasedPageRequest(from, size, sorting);

        Specification<Event> specifications = EventSpecifications.withFilters(text, categoryIds, paid,
                rangeStart, rangeEnd, onlyAvailable, EventState.PUBLISHED);

        List<Event> events = eventRepository.findAll(specifications, pageable).getContent();

        statClient.saveHit(HitCreator.createHit(request, LocalDateTime.now()));

        return events.stream().map(eventMapper::toEventDto).toList();
    }
}
