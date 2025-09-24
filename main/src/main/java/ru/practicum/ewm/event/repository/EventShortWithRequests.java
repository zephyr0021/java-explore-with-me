package ru.practicum.ewm.event.repository;

public interface EventShortWithRequests {
    EventShort getEventShort();

    Long getConfirmedRequests();
}
