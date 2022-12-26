package ru.otus.library.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.model.entity.Author;
import ru.otus.library.model.entity.Book;
import ru.otus.library.model.entity.Genre;
import ru.otus.library.repository.AuthorRepository;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.repository.GenreRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    public BookServiceImpl(final BookRepository bookRepository,
                           final AuthorRepository authorRepository,
                           final GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public Book findBookById(Long id) {
        return bookRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> findBooksByAuthorsName(final String name) {
        final Author author = authorRepository.findAuthorByName(name);
        if (author == null) {
            return Collections.emptyList();
        }
        return bookRepository.findBooksByAuthorId(author.getId());
    }

    @Override
    public List<Book> findBooksByAuthorId(Long authorId) {
        return bookRepository.findBooksByAuthorId(authorId);
    }

    @Override
    public List<Book> findBooksByTitle(String title) {
        return bookRepository.findBooksByTitle(title);
    }

    @Override
    public List<String> findAllTitles() {
        return bookRepository.findAllTitles();
    }

    @Transactional
    @Override
    public Book saveNewBook(Book book) {
        Genre genre = genreRepository.findByName(book.getGenre().getName());
        if (genre == null) {
            genre = genreRepository.save(book.getGenre());
        }
        book.setGenre(genre);

        final Set<Author> authorSet = new HashSet<>();
        for (Author author : book.getAuthors()) {
            Author checkAuthor = authorRepository.findAuthorByName(author.getName());
            if (checkAuthor == null) {
                author = authorRepository.save(author);
                author.getBooks().add(book);
                authorSet.add(author);
            } else {
                checkAuthor.getBooks().add(book);
                authorSet.add(checkAuthor);
            }
        }
        book.setAuthors(authorSet);

        return bookRepository.save(book);
    }

    @Override
    public Book updateBookTitleById(final Long id, final String newTitle) {
        final Book book = bookRepository.findById(id).orElse(null);
        if (book != null) {
            book.setTitle(newTitle);
        }
        saveNewBook(book);
        return book;
    }

    @Override
    public void deleteBookById(final Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        bookRepository.deleteAll();
    }
}
