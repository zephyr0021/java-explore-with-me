package ru.practicum.ewm.event.repository;

import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;

public interface EventShort {
    Long getId();

    String getAnnotation();

    Category getCategory();

    LocalDateTime getEventDate();

    User getInitiator();

    Boolean getPaid();

    String getTitle();

    Long getViews();

    Long getConfirmedRequests();
}
