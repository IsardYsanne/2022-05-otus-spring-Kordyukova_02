package ru.otus.library.service;

import ru.otus.library.model.entity.jpa.CommentJpa;

import java.util.List;

public interface CommentService {

    List<String> findCommentsByBookId(final Long bookId);

    boolean saveComment(final Long bookId, final String comment);

    boolean updateComment(final CommentJpa comment);

    void deleteCommentById(final Long id);

    void deleteAll();
}
