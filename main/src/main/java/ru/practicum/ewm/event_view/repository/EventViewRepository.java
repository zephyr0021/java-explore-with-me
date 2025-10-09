package ru.practicum.ewm.event_view.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.event_view.model.EventView;

public interface EventViewRepository extends JpaRepository<EventView, Long> {
    boolean existsByEventIdAndIp(Long eventId, String ip);
}
