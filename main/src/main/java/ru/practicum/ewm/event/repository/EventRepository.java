package ru.practicum.ewm.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.user.model.User;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    List<Event> findEventsByCategoryId(Long categoryId);

    Optional<Event> findEventByIdAndState(Long id, EventState state);

    Optional<Event> findEventByIdAndInitiator(Long id, User initiator);

    Page<Event> findAll (Specification<Event> specification, Pageable pageable);

    Page<EventShort> findByInitiator(User user, Pageable pageable);

    @Modifying
    @Query("UPDATE Event e SET e.views = e.views + 1 WHERE e.id = :eventId")
    void incrementView(Long eventId);}
