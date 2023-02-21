package ru.otus.library.service;

import ru.otus.library.model.entity.jpa.BookJpa;

import java.util.List;

public interface BookService {

    BookJpa findBookById(final Long id);

    List<BookJpa> findAllBooks();

    List<BookJpa> findBooksByAuthorsName(final String name);

    List<BookJpa> findBooksByAuthorId(final Long authorId);

    List<BookJpa> findBooksByTitle(final String title);

    List<String> findAllTitles();

    boolean saveNewBook(BookJpa book);

    boolean updateBookTitleById(final Long id, final String newTitle);

    void deleteBookById(final Long id);

    void deleteAll();
}
