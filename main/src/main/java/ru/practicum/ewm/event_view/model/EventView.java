package ru.practicum.ewm.event_view.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "events_views", uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "ip"}))
@Getter
@Setter
@ToString
public class EventView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(nullable = false)
    private String ip;
}
