package ru.otus.library.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.library.model.entity.Author;
import ru.otus.library.repository.AuthorRepository;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(final AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Flux<String> findAllAuthorsNames() {
        return authorRepository.findAll().map(Author::getName);
    }

    @Override
    public Flux<Author> findAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public Mono<Author> saveAuthor(Author author) {
        return authorRepository.findAuthorByName(author.getName()).switchIfEmpty(authorRepository.save(author));
    }

    @Override
    public Mono<Author> updateAuthorById(String authorId, String newAuthorName) {
        return authorRepository.findAuthorById(authorId).map(author -> {
            author.setName(newAuthorName);
            return author;
        }).flatMap(authorRepository::save).switchIfEmpty(Mono.error(new RuntimeException()));
    }

    @Override
    public Mono<Long> deleteAuthorById(String id) {
        return authorRepository.deleteAuthorById(id);
    }

    @Override
    public void deleteAll() {
        authorRepository.deleteAll();
    }
}