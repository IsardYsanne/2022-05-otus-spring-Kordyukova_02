package ru.otus.library.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.library.model.entity.Comment;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {

    Flux<Comment> findAllById(String id);

    Flux<Comment> findCommentsByBookId(String id);

    Mono<Long> deleteCommentById(String id);

    Mono<Long> deleteCommentByBookId(String bookId);

    Mono<Comment> save(Mono<Comment> comment);
}
