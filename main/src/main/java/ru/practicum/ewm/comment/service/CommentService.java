package ru.practicum.ewm.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.CommentRequest;
import ru.practicum.ewm.comment.mapper.CommentMapper;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.repository.CommentRepository;
import ru.practicum.ewm.common.exception.ConflictException;
import ru.practicum.ewm.common.exception.NotFoundException;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.service.PrivateEventService;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserService userService;
    private final PrivateEventService privateEventService;

    public CommentDto getComment(Long userId, Long commentId) {
        userService.getUserModel(userId);

        return commentRepository.findById(commentId)
                .map(commentMapper::toCommentDto)
                .orElseThrow(() -> new NotFoundException("Comment with id = " + commentId + " was not found"));
    }

    public Comment getCommentModel(Long userId, Long commentId) {
        userService.getUserModel(userId);

        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id = " + commentId + " was not found"));
    }

    public List<CommentDto> getEventComments(Long userId, Long eventId) {
        userService.getUserModel(userId);
        privateEventService.getEventModel(eventId);

        return commentRepository.findAllByEventIdOrderByCreatedOnDesc(eventId).stream()
                .map(commentMapper::toCommentDto)
                .toList();
    }

    @Transactional
    public CommentDto createComment(Long userId, Long eventId, CommentRequest newCommentRequest) {
        User author = userService.getUserModel(userId);
        Event event = privateEventService.getEventModel(eventId);
        Comment comment = commentMapper.toComment(newCommentRequest);

        comment.setAuthor(author);
        comment.setEvent(event);
        comment.setCreatedOn(LocalDateTime.now());

        comment = commentRepository.save(comment);

        return commentMapper.toCommentDto(comment);

    }

    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = getCommentModel(userId, commentId);

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ConflictException("Comment cannot be deleted");
        }

        commentRepository.delete(comment);
    }

    @Transactional
    public CommentDto updateComment(Long userId, Long commentId, CommentRequest commentRequest) {
        Comment newComment = getCommentModel(userId, commentId);

        if (!newComment.getAuthor().getId().equals(userId)) {
            throw new ConflictException("Comment cannot be updated");
        }

        newComment = commentMapper.updateComment(commentRequest, newComment);

        newComment = commentRepository.save(newComment);

        return commentMapper.toCommentDto(newComment);
    }
}
