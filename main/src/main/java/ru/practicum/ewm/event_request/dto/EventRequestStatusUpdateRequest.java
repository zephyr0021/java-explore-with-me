package ru.practicum.ewm.event_request.dto;

import lombok.Data;
import ru.practicum.ewm.event_request.model.EventRequestStatus;

import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;

    private EventRequestStatus status;
}
