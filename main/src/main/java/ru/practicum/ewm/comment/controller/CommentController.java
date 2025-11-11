package ru.practicum.ewm.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.CommentRequest;
import ru.practicum.ewm.comment.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{commentId}")
    public CommentDto getComment(@PathVariable("userId") Long userId, @PathVariable("commentId") Long commentId) {
        return commentService.getComment(userId, commentId);
    }

    @GetMapping
    public List<CommentDto> getEventComments(@PathVariable("userId") Long userId, @RequestParam Long eventId) {
        return commentService.getEventComments(userId, eventId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@PathVariable Long userId, @RequestParam Long eventId,
                                 @RequestBody @Valid CommentRequest request) {
        return commentService.createComment(userId, eventId, request);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable("userId") Long userId, @PathVariable("commentId") Long commentId) {
        commentService.deleteComment(userId, commentId);
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@PathVariable("userId") Long userId, @PathVariable("commentId") Long commentId,
                                    @RequestBody @Valid CommentRequest request) {
        return commentService.updateComment(userId, commentId, request);
    }

}
