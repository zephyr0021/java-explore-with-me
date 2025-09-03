package ru.practicum.ewm.common.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class ErrorResponse {
    private final String status;
    private final String reason;
    private final String message;
    private final LocalDateTime timestamp;
}
