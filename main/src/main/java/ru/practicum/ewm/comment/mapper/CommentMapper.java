package ru.practicum.ewm.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.CommentRequest;
import ru.practicum.ewm.comment.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toComment(CommentRequest request);

    @Mapping(target = "createdAt", source = "createdOn")
    @Mapping(target = "eventId", source = "event.id")
    CommentDto toCommentDto(Comment comment);

    Comment updateComment(CommentRequest request, @MappingTarget Comment comment);
}
