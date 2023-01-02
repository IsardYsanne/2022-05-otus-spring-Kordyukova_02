package ru.otus.library.service;

import org.springframework.stereotype.Service;
import ru.otus.library.model.entity.Book;
import ru.otus.library.repository.BookRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl implements GenreService {

    private BookRepository bookRepository;

    public GenreServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<String> findAllGenres() {
        return bookRepository.findAll().stream().map(Book::getGenre).collect(Collectors.toList());
    }

    @Override
    public void saveGenre(String genreName, String bookId) {
        Book book = bookRepository.findBookById(bookId);
        if (book == null) {
            return;
        }

        book.setGenre(genreName);
        bookRepository.save(book);
    }

    @Override
    public void deleteAll() {
        bookRepository.findAll().forEach(book -> book.setGenre(null));
    }
}
