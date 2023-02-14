package ru.otus.library.service;

import ru.otus.library.model.entity.Author;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AuthorService {

    Flux<String> findAllAuthorsNames();

    Flux<Author> findAllAuthors();

    Mono<Author> saveAuthor(Author author);

    Mono<Author> updateAuthorById(final String authorId, final String newAuthorName);

    Mono<Long> deleteAuthorById(String id);

    void deleteAll();
}
