package ru.otus.library.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.model.entity.jpa.AuthorJpa;
import ru.otus.library.model.entity.jpa.BookJpa;
import ru.otus.library.model.entity.jpa.GenreJpa;
import ru.otus.library.repository.jpa.AuthorRepositoryJpa;
import ru.otus.library.repository.jpa.BookRepositoryJpa;
import ru.otus.library.repository.jpa.GenreRepositoryJpa;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BookServiceImpl implements BookService {

    private BookRepositoryJpa bookRepository;

    private AuthorRepositoryJpa authorRepository;

    private GenreRepositoryJpa genreRepository;

    public BookServiceImpl(BookRepositoryJpa bookRepository,
                           AuthorRepositoryJpa authorRepository,
                           GenreRepositoryJpa genreRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public BookJpa findBookById(Long id) {
        return bookRepository.findById(id).orElseThrow();
    }

    @Override
    public List<BookJpa> findAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public List<BookJpa> findBooksByAuthorsName(final String name) {
        final AuthorJpa author = authorRepository.findAuthorByName(name);
        if (author == null) {
            return Collections.emptyList();
        }
        return bookRepository.findBooksByAuthorId(author.getId());
    }

    @Override
    public List<BookJpa> findBooksByAuthorId(Long authorId) {
        return bookRepository.findBooksByAuthorId(authorId);
    }

    @Override
    public List<BookJpa> findBooksByTitle(String title) {
        return bookRepository.findBooksByTitle(title);
    }

    @Override
    public List<String> findAllTitles() {
        return bookRepository.findAllTitles();
    }

    @Transactional
    @Override
    public boolean saveNewBook(BookJpa book) {
        GenreJpa genre = genreRepository.findByName(book.getGenre().getName());
        if (genre == null) {
            genre = genreRepository.save(book.getGenre());
        }
        book.setGenre(genre);

        final Set<AuthorJpa> authorSet = new HashSet<>();
        for (AuthorJpa author : book.getAuthors()) {
            AuthorJpa checkAuthor = authorRepository.findAuthorByName(author.getName());
            if (checkAuthor == null) {
                author = authorRepository.save(author);
                authorSet.add(author);
            } else {
                authorSet.add(checkAuthor);
            }
        }
        book.setAuthors(authorSet);

        if (isBookDuplicate(book)) {
            return false;
        }

        book = bookRepository.save(book);
        return book.getId() != null;
    }

    @Transactional
    @Override
    public boolean updateBookTitleById(final Long id, final String newTitle) {
        final BookJpa book = bookRepository.findById(id).orElse(null);
        if (book != null) {
            book.setTitle(newTitle);
            return true;
        }
        return false;
    }

    @Override
    public void deleteBookById(final Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        bookRepository.deleteAll();
    }

    private boolean isBookDuplicate(BookJpa book) {
        List<BookJpa> booksWithSameTitle = bookRepository.findBooksByTitle(book.getTitle());
        if (!booksWithSameTitle.isEmpty()) {
            for (BookJpa bookFromDB : booksWithSameTitle) {
                if (book.getGenre().equals(bookFromDB.getGenre())
                        && book.getAuthors().containsAll(bookFromDB.getAuthors())) {
                    return true;
                }
            }
        }
        return false;
    }
}
