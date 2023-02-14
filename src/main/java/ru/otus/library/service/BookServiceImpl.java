package ru.otus.library.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.library.model.entity.Author;
import ru.otus.library.model.entity.Book;
import ru.otus.library.model.entity.Genre;
import ru.otus.library.repository.AuthorRepository;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.repository.CommentRepository;
import ru.otus.library.repository.GenreRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final CommentRepository commentRepository;

    public BookServiceImpl(final BookRepository bookRepository,
                           final AuthorRepository authorRepository,
                           final GenreRepository genreRepository,
                           final CommentRepository commentRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public Flux<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Flux<Book> findBooksByAuthorsName(String name) {
        return bookRepository.findAll().filter(book -> {
            if (book.getAuthors() != null) {
                for (Author author : book.getAuthors()) {
                    if (name.equals(author.getName())) {
                        return true;
                    }
                }
            }
            return false;
        });
    }

    @Override
    public Mono<Book> findBookById(String id) {
        return bookRepository.findBookById(id);
    }

    @Override
    public Mono<Book> saveBook(Book book) {
        Mono<Genre> genre = genreRepository.save(book.getGenre());

        Mono<Set<Author>> authors = Flux.fromIterable(book.getAuthors())
                .flatMap(authorRepository::save)
                .collect(Collectors.toSet());

        return Mono.zip(authors, genre, (a, g) -> new Book(book.getTitle(), g, a))
                .flatMap(bookRepository::save);
    }

    @Override
    public Mono<Book> updateBookTitleById(String id, String newTitle) {
        return bookRepository.findById(id).map(book -> {
                    book.setTitle(newTitle);
                    return book;
                })
                .flatMap(bookRepository::save)
                .switchIfEmpty(Mono.error(new RuntimeException()));
    }

    @Override
    public Mono<Book> updateBook(Mono<Book> book) {
        return bookRepository.save(book);
    }

    @Override
    public Mono<Long> deleteBookById(String id) {
        commentRepository.deleteCommentByBookId(id);
        return bookRepository.deleteBookById(id);
    }

    @Override
    public void deleteAll() {
        bookRepository.deleteAll();
    }
}