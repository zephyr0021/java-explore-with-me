package ru.practicum.ewm.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findEventsByCategoryId(Long categoryId);

    @Query("SELECT e AS event, COUNT(er.id) AS confirmedRequests FROM Event e " +
            "LEFT JOIN EventRequest er ON e.id = er.event.id AND er.status = 'CONFIRMED' " +
            "WHERE e.id = :id AND e.state = :state " +
            "GROUP BY e")
    Optional<EventWithRequests> findEventByIdAndState(Long id, EventState state);
}
