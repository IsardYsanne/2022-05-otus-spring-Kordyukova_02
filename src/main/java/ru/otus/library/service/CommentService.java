package ru.otus.library.service;

import ru.otus.library.model.entity.Comment;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CommentService {

    Flux<String> findAllCommentsTexts(String bookId);

    Flux<Comment> findAllComments(String id);

    Mono<Comment> saveComment(String bookId, String comment);

    Mono<Comment> updateComment(Mono<Comment> comment);

    Mono<Comment> updateComment(String id, String newComment);

    Mono<Long> deleteCommentById(String id);

    void deleteAll();
}
