package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.repository.EventRepository;


@Service
@RequiredArgsConstructor
class EventViewsService {
    private final EventRepository eventRepository;

    void addViewToEvent(Long id) {
        eventRepository.incrementView(id);
    }
}
