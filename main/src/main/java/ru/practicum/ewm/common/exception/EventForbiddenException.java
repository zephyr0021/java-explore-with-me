package ru.practicum.ewm.common.exception;

public class EventForbiddenException extends RuntimeException {
    public EventForbiddenException(String message) {
        super(message);
    }
}
