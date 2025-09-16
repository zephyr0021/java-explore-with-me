package ru.practicum.ewm.event_request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.event_request.model.EventRequest;
import ru.practicum.ewm.event_request.model.EventRequestStatus;

import java.util.List;

public interface EventRequestRepository extends JpaRepository<EventRequest, Long> {
    List<EventRequest> findAllByEventIdAndStatus(Long eventId, EventRequestStatus status);
}
