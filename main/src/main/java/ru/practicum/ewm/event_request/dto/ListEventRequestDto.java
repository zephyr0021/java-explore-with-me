package ru.practicum.ewm.event_request.dto;

import lombok.Data;

import java.util.List;

@Data
public class ListEventRequestDto {
    private List<EventRequestDto> confirmedRequests;

    private List<EventRequestDto> rejectedRequests;
}
