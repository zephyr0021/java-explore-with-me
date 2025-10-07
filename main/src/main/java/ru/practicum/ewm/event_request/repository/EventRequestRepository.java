package ru.practicum.ewm.event_request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event_request.model.EventRequest;
import ru.practicum.ewm.event_request.model.EventRequestStatus;
import ru.practicum.ewm.user.model.User;

import java.util.List;
import java.util.Optional;

public interface EventRequestRepository extends JpaRepository<EventRequest, Long> {
    List<EventRequest> findAllByEventIdAndStatus(Long eventId, EventRequestStatus status);

    List<EventRequest> findAllByRequester(User user);

    List<EventRequest> findAllByStatus(EventRequestStatus status);

    Optional<EventRequest> findByEventIdAndRequesterId(Long eventId, Long requesterId);

    Integer countEventRequestByEvent(Event event);

    Optional<EventRequest> findByIdAndRequesterId(Long eventRequestId, Long requesterId);

    List<EventRequest> findAllByEvent(Event event);

    List<EventRequest> findAllByEventAndIdIn(Event event, List<Long> ids);
}
