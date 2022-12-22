package ru.otus.library.service;

import java.util.List;

public interface CommentService {

    List<String> findCommentsByBookId(String bookId);

    boolean saveComment(String bookId, String userName, String comment);

    void deleteAll();
}
