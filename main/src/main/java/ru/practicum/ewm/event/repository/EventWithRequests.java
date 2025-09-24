package ru.practicum.ewm.event.repository;

import ru.practicum.ewm.event.model.Event;

public interface EventWithRequests {
    Event getEvent();

    Long getConfirmedRequests();
}
