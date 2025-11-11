package ru.practicum.ewm.event.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Formula;
import org.hibernate.proxy.HibernateProxy;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
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
    private LocalDateTime eventDate;

    @Embedded
    private Location location;

    @Column(nullable = false)
    private Boolean paid;

    @Column(nullable = false, name = "participant_limit")
    private Long participantLimit;

    @Column(nullable = false, name = "request_moderation")
    private Boolean requestModeration;

    @Column(nullable = false, name = "event_title")
    private String title;

    @Column(nullable = false, name = "created_on")
    private LocalDateTime createdOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @Formula("(SELECT COUNT(ev.id) FROM events_views ev WHERE ev.event_id = id)")
    private Long views;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "event_state")
    @Enumerated(EnumType.STRING)
    private EventState state;

    @Formula("(SELECT COUNT(er.id) FROM events_requests er WHERE er.event_id = id AND er.status='CONFIRMED')")
    private Long confirmedRequests;

    @OneToMany(mappedBy = "event")
    @ToString.Exclude
    private List<Comment> comments;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Event event = (Event) o;
        return getId() != null && Objects.equals(getId(), event.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
