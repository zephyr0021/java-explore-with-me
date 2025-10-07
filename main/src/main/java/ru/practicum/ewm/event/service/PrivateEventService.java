package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.service.CategoryService;
import ru.practicum.ewm.common.custom.OffsetBasedPageRequest;
import ru.practicum.ewm.common.exception.EventForbiddenException;
import ru.practicum.ewm.common.exception.NotFoundException;
import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.NewEventRequest;
import ru.practicum.ewm.event.dto.PublicUpdateEventRequest;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrivateEventService {
    private final EventRepository eventRepository;
    private final UserService userService;
    private final EventMapper eventMapper;
    private final CategoryService categoryService;

    public List<EventDto> getUserEvents(Long userId, Integer from, Integer size) {
        User user = userService.getUserModel(userId);
        Pageable pageable = new OffsetBasedPageRequest(from, size, Sort.by("id"));

        return eventRepository.findByInitiator(user, pageable).getContent()
                .stream()
                .map(eventMapper::toEventDto).toList();

    }

    public EventFullDto getUserEvent(Long userId, Long eventId) {
        User user = userService.getUserModel(userId);

        return eventRepository.findEventByIdAndInitiator(eventId, user)
                .map(eventMapper::toEventFullDto)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
    }

    public Event getEventModel(Long eventId) {

        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
    }

    public Event getUserEventModel(Long userId, Long eventId) {
        User user = userService.getUserModel(userId);

        return eventRepository.findEventByIdAndInitiator(eventId, user)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
    }

    @Transactional
    public EventFullDto updateUserEvent(Long userId, Long eventId, PublicUpdateEventRequest request) {
        userService.getUserModel(userId);

        Event newEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        if (newEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new EventForbiddenException("Impossible to update the event because " +
                    "its start date is earlier than 2 hour after now.");
        }

        EventState state = newEvent.getState();

        if (state == EventState.PUBLISHED) {
            throw new EventForbiddenException("Only pending or canceled events can be changed");
        }

        Long categoryId = request.getCategory();

        if (categoryId != null) {
            Category category = categoryService.getCategoryModel(categoryId);
            newEvent.setCategory(category);
        }

        newEvent = eventMapper.updateEvent(request, newEvent);

        newEvent = eventRepository.save(newEvent);

        return eventMapper.toEventFullDto(newEvent);

    }

    @Transactional
    public EventFullDto createEvent(Long userId, NewEventRequest request) {
        LocalDateTime eventDate = request.getEventDate();
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new EventForbiddenException("Field: eventDate. Error: должно содержать дату, " +
                    "которая еще не наступила. Value: " + eventDate);
        }

        User user = userService.getUserModel(userId);
        Category category = categoryService.getCategoryModel(request.getCategory());
        Event event = eventMapper.toEvent(request);

        event.setCategory(category);
        event.setInitiator(user);
        event.setCreatedOn(LocalDateTime.now());
        event.setViews(0L);
        event.setState(EventState.PENDING);
        event.setConfirmedRequests(0L);

        event = eventRepository.save(event);

        return eventMapper.toEventFullDto(event);
    }


}
