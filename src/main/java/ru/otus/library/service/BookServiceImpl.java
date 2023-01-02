package ru.otus.library.service;

import org.springframework.stereotype.Service;
import ru.otus.library.model.entity.Book;
import ru.otus.library.repository.BookRepository;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> findBooksByAuthorsName(String name) {
        return bookRepository.findBooksByAuthors(name);
    }

    @Override
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public boolean saveNewBook(Book book) {
        book = bookRepository.save(book);
        return book.getId() != null;
    }

    @Override
    public boolean updateBookTitleById(String id, String newTitle) {
        Book book = bookRepository.findBookById(id);
        if (book == null) {
            return false;
        }

        book.setTitle(newTitle);
        book = bookRepository.save(book);
        return book.getId() != null;
    }

    @Override
    public boolean deleteBookById(String bookId) {
        final int result = bookRepository.deleteBookById(bookId);
        return result > 0;
    }

    @Override
    public void deleteAll() {
        bookRepository.deleteAll();
    }
}
