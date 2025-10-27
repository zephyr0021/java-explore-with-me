package ru.practicum.ewm.comment.dto;

import lombok.Data;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;

    private String text;

    private UserShortDto author;

    private Long eventId;

    private LocalDateTime createdAt;
}
