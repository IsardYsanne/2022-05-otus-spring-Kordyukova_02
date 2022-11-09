package ru.otus.library.repository;

import ru.otus.library.model.entity.Author;
import ru.otus.library.model.entity.Book;

import java.util.List;

public interface BookRepository {

    Book findBookById(final Long id);

    List<Book> findAllBooks();

    List<Book> findBooksByAuthor(final Author author);

    List<Book> findBooksByTitle(final String title);

    List<String> findAllTitles();

    Book saveBook(final Book book);

    boolean deleteBookById(final Long id);

    int deleteAll();
}
