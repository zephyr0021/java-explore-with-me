package ru.practicum.ewm.event.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventSpecifications {
    public static Specification<Event> withFilters(String text, List<Long> categoryIds, Boolean paid,
                                                   LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                   Boolean onlyAvailable, EventState state) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (state != null) {
                predicates.add(cb.equal(root.get("state"), state));
            }

            if (text != null && !text.isEmpty()) {
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("annotation")), "%" + text.toLowerCase() + "%"),
                        cb.like(cb.lower(root.get("description")), "%" + text.toLowerCase() + "%")
                ));
            }

            if (categoryIds != null && !categoryIds.isEmpty()) {
                predicates.add(root.get("category").get("id").in(categoryIds));
            }

            if (paid != null) {
                predicates.add(cb.equal(root.get("paid"), paid));
            }

            if (rangeStart == null && rangeEnd == null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("eventDate"), LocalDateTime.now()));
            }

            if (rangeStart != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
            }

            if (rangeEnd != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
            }

            if (onlyAvailable != null) {
                predicates.add(cb.lessThan(root.get("confirmedRequests"), root.get("participantLimit")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));

        };
    }

    public static Specification<Event> withFilters(List<Long> usersIds, List<EventState> states,
                                                   List<Long> categoriesIds, LocalDateTime rangeStart,
                                                   LocalDateTime rangeEnd) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (usersIds != null && !usersIds.isEmpty()) {
                predicates.add(root.get("initiator").get("id").in(usersIds));
            }

            if (states != null && !states.isEmpty()) {
                predicates.add(root.get("state").in(states));
            }

            if (categoriesIds != null && !categoriesIds.isEmpty()) {
                predicates.add(root.get("category").get("id").in(categoriesIds));
            }

            if (rangeStart == null && rangeEnd == null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("eventDate"), LocalDateTime.now()));
            }

            if (rangeStart != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
            }

            if (rangeEnd != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
