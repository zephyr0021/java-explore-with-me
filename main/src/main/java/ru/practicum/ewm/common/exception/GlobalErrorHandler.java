package ru.practicum.ewm.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.ewm.common.response.ErrorResponse;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalErrorHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException e) {
        String field = e.getBindingResult().getFieldError().getField();
        String messageError = e.getBindingResult().getFieldError().getDefaultMessage();
        String fieldValue = e.getBindingResult().getFieldError().getRejectedValue() != null ? e.getBindingResult()
                .getFieldError().getRejectedValue().toString() : "null";
        return new ErrorResponse("BAD_REQUEST",
                "Incorrectly made request.",
                String.format("Field: %s. Error: %s. Value: %s", field, messageError, fieldValue),
                LocalDateTime.now());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentTypeMismatchException e) {
        return new ErrorResponse("BAD_REQUEST",
                "Incorrectly made request.",
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return new ErrorResponse("CONFLICT", "Integrity constraint has been violated.", e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler(CategoryNotEmptyException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleCategoryNotEmptyException(CategoryNotEmptyException e) {
        return new ErrorResponse("CONFLICT", "For the requested operation the conditions are not met.",
                e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        return new ErrorResponse("NOT_FOUND", "The required object was not found.", e.getMessage(),
                LocalDateTime.now());
    }


}
