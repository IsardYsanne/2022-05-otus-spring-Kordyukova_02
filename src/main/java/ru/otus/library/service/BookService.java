package ru.otus.library.service;

import ru.otus.library.model.entity.Book;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookService {

    Flux<Book> findAllBooks();

    Flux<Book> findBooksByAuthorsName(String name);

    Mono<Book> findBookById(String id);

    Mono<Book> saveBook(Book book);

    Mono<Book> updateBookTitleById(String id, String newTitle);

    Mono<Book> updateBook(Mono<Book> book);

    Mono<Long> deleteBookById(String id);

    void deleteAll();
}
