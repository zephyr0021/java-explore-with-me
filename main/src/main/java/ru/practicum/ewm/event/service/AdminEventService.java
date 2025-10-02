package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.service.CategoryService;
import ru.practicum.ewm.common.custom.OffsetBasedPageRequest;
import ru.practicum.ewm.common.exception.EventForbiddenException;
import ru.practicum.ewm.common.exception.NotFoundException;
import ru.practicum.ewm.event.dto.AdminUpdateEventRequest;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.AdminEventStateAction;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.repository.EventSpecifications;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminEventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CategoryService categoryService;

    public List<EventFullDto> getEvents(List<Long> usersIds, List<EventState> states, List<Long> categoriesIds,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        Pageable pageable = new OffsetBasedPageRequest(from, size, Sort.unsorted());
        Specification<Event> specification = EventSpecifications.withFilters(usersIds, states, categoriesIds,
                rangeStart, rangeEnd);

        List<Event> events = eventRepository.findAll(specification, pageable).getContent();

        return events.stream().map(eventMapper::toEventFullDto).toList();
    }

    @Transactional
    public EventFullDto updateEvent(Long eventId, AdminUpdateEventRequest request) {
        Event newEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        if (newEvent.getPublishedOn() != null &&
                newEvent.getEventDate().isBefore(newEvent.getPublishedOn().plusHours(1))) {
            throw new EventForbiddenException("Impossible to update the event because " +
                    "its start date is earlier than 1 hour after publication.");
        }

        EventState state = newEvent.getState();
        AdminEventStateAction stateAction = request.getStateAction();


        if (stateAction == AdminEventStateAction.PUBLISH_EVENT && state != EventState.PENDING) {
            throw new EventForbiddenException("Cannot publish the event because it's not in the right state: " + state);
        }

        if (stateAction == AdminEventStateAction.REJECT_EVENT && state == EventState.PUBLISHED) {
            throw new EventForbiddenException("Cannot reject the event because it's not in the right state: " + state);
        }

        Long categoryId = request.getCategory();

        if (categoryId != null) {
            Category category = categoryService.getCategoryModel(categoryId);
            newEvent.setCategory(category);
        }

        if (stateAction == AdminEventStateAction.PUBLISH_EVENT) {
            newEvent.setPublishedOn(LocalDateTime.now());
        }

        newEvent = eventMapper.updateEvent(request, newEvent);

        newEvent = eventRepository.save(newEvent);

        return eventMapper.toEventFullDto(newEvent);
    }


}
