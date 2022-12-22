package ru.otus.library.service;

import ru.otus.library.model.entity.Book;

import java.util.List;

public interface BookService {

    List<Book> findBooksByAuthorsName(String name);

    List<Book> findAllBooks();

    boolean saveNewBook(Book book);

    boolean updateBookTitleById(String id, String newTitle);

    boolean deleteBookById(String bookId);

    void deleteAll();
}
