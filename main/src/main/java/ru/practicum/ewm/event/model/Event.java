package ru.practicum.ewm.event.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event_request.model.EventRequest;
import ru.practicum.ewm.user.model.User;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "events")
@Getter
@Setter
@ToString
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, name = "event_date")
    private OffsetDateTime eventDate;

    @Embedded
    private Location location;

    @Column(nullable = false)
    private Boolean paid;

    @Column(nullable = false, name = "participant_limit")
    private Integer participantLimit;

    @Column(nullable = false, name = "request_moderation")
    private Boolean requestModeration;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, name = "created_on")
    private OffsetDateTime createdOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @Column(nullable = false)
    private Integer views;

    @Column(name = "published_on")
    private OffsetDateTime publishedOn;

    @Column(name = "event_state")
    @Enumerated(EnumType.STRING)
    private EventState state;

    @Transient
    private List<EventRequest> requests;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
