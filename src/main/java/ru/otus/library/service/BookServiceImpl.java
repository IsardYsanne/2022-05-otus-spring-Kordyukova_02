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
    public List<Book> findAllBooks() {
        return bookRepository.findAllBooks();
    }

    @Override
    public List<Book> findBooksByAuthorsName(final String name) {
        final Author author = authorRepository.findAuthorByName(name);
        if (author == null) {
            return Collections.emptyList();
        }
        return bookRepository.findBooksByAuthor(author);
    }

    @Transactional
    @Override
    public boolean saveNewBook(Book book) {
        Genre genre = genreRepository.findGenreByName(book.getGenre().getName());
        if (genre == null) {
            genre = genreRepository.saveGenre(book.getGenre());
        }
        book.setGenre(genre);

        final Set<Author> authorSet = new HashSet<>();
        for (Author author : book.getAuthors()) {
            Author checkAuthor = authorRepository.findAuthorByName(author.getName());
            if (checkAuthor == null) {
                author = authorRepository.saveAuthor(author);
                authorSet.add(author);
            } else {
                authorSet.add(checkAuthor);
            }
        }
        book.setAuthors(authorSet);

        if (isBookDuplicate(book)) {
            return false;
        }

        book = bookRepository.saveBook(book);
        return book.getId() != null;
    }

    @Transactional
    @Override
    public boolean updateBookTitleById(final Long id, final String newTitle) {
        final Book book = bookRepository.findBookById(id);
        if (book != null) {
            book.setTitle(newTitle);
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean deleteBookById(final Long id) {
        return bookRepository.deleteBookById(id);
    }

    @Override
    public void deleteAll() {
        bookRepository.deleteAll();
    }

    private boolean isBookDuplicate(Book book) {
        List<Book> booksWithSameTitle = bookRepository.findBooksByTitle(book.getTitle());
        if (!booksWithSameTitle.isEmpty()) {
            for (Book bookFromDB : booksWithSameTitle) {
                if (book.getGenre().equals(bookFromDB.getGenre())
                        && book.getAuthors().containsAll(bookFromDB.getAuthors())) {
                    return true;
                }
            }
        }
        return false;
    }
}
