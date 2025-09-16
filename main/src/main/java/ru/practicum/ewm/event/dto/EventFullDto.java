package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.time.OffsetDateTime;

@Data
public class EventFullDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String annotation;

    private CategoryDto category;

    private Integer confirmedRequests;

    private OffsetDateTime createdOn;

    private String description;

    private OffsetDateTime eventDate;

    private UserShortDto initiator;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private OffsetDateTime publishedOn;

    private Boolean requestModeration;

    private EventState state;

    private String title;

    private Integer views;
}
