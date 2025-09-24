package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
public class EventDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String annotation;

    private CategoryDto category;

    private Long confirmedRequests;

    private LocalDateTime eventDate;

    private UserShortDto initiator;

    private Boolean paid;

    private Long views;
}
