package ru.otus.library.service;

import ru.otus.library.dto.BookDto;
import ru.otus.library.model.entity.Book;

import java.util.List;

public interface BookService {

    Book findBookById(final Long id);

    List<Book> findAllBooks();

    List<Book> findBooksByAuthorsName(final String name);

    List<Book> findBooksByAuthorId(final Long authorId);

    List<Book> findBooksByTitle(final String title);

    List<String> findAllTitles();

    Book saveNewBook(Book book);

    Book updateBook(final BookDto bookDto);

    Book updateBookImage(final BookDto bookDto);

    void deleteBookById(final Long id);

    void deleteAll();
}
