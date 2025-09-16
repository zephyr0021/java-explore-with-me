package ru.practicum.ewm.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findEventsByCategoryId(Long categoryId);

    Optional<Event> findEventByIdAndState(Long id, EventState state);
}
