package ru.otus.library.service;

import ru.otus.library.model.entity.Comment;

import java.util.Set;

public interface CommentService {

    Set<Comment> findCommentsByBookId(final Long bookId);

    boolean saveComment(final Long bookId, final String comment);

    boolean updateComment(final Comment comment);

    boolean deleteCommentById(final Long id);

    void deleteAll();
}
