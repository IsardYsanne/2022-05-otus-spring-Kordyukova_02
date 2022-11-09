package ru.otus.library.repository;

import ru.otus.library.model.entity.Comment;

public interface CommentRepository {

    Comment findCommentById(final Long id);

    Comment saveComment(final Comment comment);

    boolean deleteCommentById(final Long id);

    void deleteAll();
}
