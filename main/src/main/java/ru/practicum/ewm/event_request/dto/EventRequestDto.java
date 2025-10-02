package ru.practicum.ewm.event_request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.practicum.ewm.event_request.model.EventRequestStatus;

import java.time.LocalDateTime;

@Data
public class EventRequestDto {
    private LocalDateTime created;

    private Long event;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private Long requester;

    private EventRequestStatus status;
}
