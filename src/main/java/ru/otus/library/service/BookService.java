package ru.otus.library.service;

import ru.otus.library.model.entity.Book;

import java.util.List;

public interface BookService {

    List<Book> findAllBooks();

    List<Book> findBooksByAuthorsName(final String name);

    boolean saveNewBook(Book book);

    boolean updateBookTitleById(final Long id, final String newTitle);

    boolean deleteBookById(final Long id);

    void deleteAll();
}
