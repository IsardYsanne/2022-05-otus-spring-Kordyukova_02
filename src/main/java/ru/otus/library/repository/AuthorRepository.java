package ru.otus.library.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.library.model.entity.Author;

@Repository
public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {

    Mono<Author> findAuthorById(String id);

    Mono<Author> findAuthorByName(final String name);

    Flux<Author> findAll();

    Mono<Author> save(Mono<Author> author);

    Mono<Long> deleteAuthorById(String id);
}
