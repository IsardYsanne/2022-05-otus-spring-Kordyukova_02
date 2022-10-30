package ru.otus.library.service;

import ru.otus.library.model.entity.Book;

import java.util.List;

public interface BookService {

    List<Book> getAllBooks();

    List<Book> getBooksByAuthorsName(final String name);

    boolean addNewBook(Book book);

    boolean updateBookTitleById(final Long id, final String newTitle);

    boolean deleteBookById(final Long id);

    void deleteAll();
}
