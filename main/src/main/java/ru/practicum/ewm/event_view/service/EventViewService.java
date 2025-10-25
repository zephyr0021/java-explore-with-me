package ru.practicum.ewm.event_view.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event_view.model.EventView;
import ru.practicum.ewm.event_view.repository.EventViewRepository;

@Service
@RequiredArgsConstructor
public class EventViewService {
    private final EventViewRepository eventViewRepository;

    public void addViewToEvent(Long eventId, String ip) {
        if (!eventViewRepository.existsByEventIdAndIp(eventId, ip)) {
            EventView view = new EventView();
            view.setEventId(eventId);
            view.setIp(ip);
            eventViewRepository.save(view);
        }
    }
}
