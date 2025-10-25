package ru.practicum.ewm.exception;

public class BadDateException extends RuntimeException {
    public BadDateException(String message) {
        super(message);
    }
}
