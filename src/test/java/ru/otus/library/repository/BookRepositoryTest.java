package ru.otus.library.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.library.model.entity.Author;
import ru.otus.library.model.entity.Book;
import ru.otus.library.model.entity.Genre;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@RunWith(SpringRunner.class)
@DataMongoTest
public class BookRepositoryTest {

    private static final String TEST_TITLE_1 = "testTitle";

    private static final String TEST_TITLE_2 = "testTitle2";

    private static final String TEST_AUTHOR_1 = "testAuthor";

    private static final String TEST_AUTHOR_2 = "testAuthor2";

    private static final String TEST_GENRE_1 = "testGenre";

    private static final String TEST_GENRE_2 = "testGenre2";

    private static final String TEST_GENRE_3 = "testGenre3";

    private static final String TEST_AUTHOR_3 = "testAuthor3";

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Before
    public void init() {
        bookRepository.deleteAll().block();
        authorRepository.deleteAll().block();
        genreRepository.deleteAll().block();
    }

    private Mono<Book> saveTestBookToDataBase(String title, String authorName, String genreName) {
        Author author = new Author();
        author.setName(authorName);
        Mono<Author> authorMono = authorRepository.save(author);
        Set<Author> authors = new HashSet<>();
        authors.add(authorMono.block());

        Genre genre = saveTestGenre(genreName).block();

        Book book = new Book();
        book.setTitle(title);
        book.setAuthors(authors);
        book.setGenre(genre);

        return bookRepository.save(book);
    }

    @Test
    public void findAllBooksTest() {
        Flux<Book> books = bookRepository.findAll();
        StepVerifier
                .create(books)
                .verifyComplete();

        Book book1 = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1).block();
        Book book2 = saveTestBookToDataBase(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2).block();

        Flux<Book> books2 = bookRepository.findAll();
        StepVerifier
                .create(books2)
                .expectNext(book1, book2)
                .expectComplete()
                .verify();
    }

    @Test
    public void findAllTitlesTest() {
        saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        saveTestBookToDataBase(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        Flux<String> titles = bookRepository.findAll().map(Book::getTitle);

        StepVerifier.create(titles).expectComplete().verify();
    }

    @Test
    public void findBookByAuthorIdTest() {
        Book expectedBook = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1).block();
        saveTestBookToDataBase(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2).block();
        Iterator<Author> iterator = expectedBook.getAuthors().iterator();
        Author author = iterator.next();

        Flux<Book> books = bookRepository.findAllByAuthorsId(author.getId());

        Flux<Book> booksAll = bookRepository.findAll();
        StepVerifier
                .create(booksAll)
                .expectNextCount(2)
                .expectComplete()
                .verify();

        StepVerifier
                .create(books)
                .assertNext(book -> assertThat(book).isEqualTo(expectedBook))
                .expectComplete()
                .verify();
    }

    @Test
    public void findBookByIdTest() {
        Book expectedBook = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1).block();
        saveTestBookToDataBase(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2).block();

        String id = expectedBook.getId();
        Mono<Book> resultBook = bookRepository.findBookById(id);

        StepVerifier
                .create(resultBook)
                .assertNext(book -> assertThat(book).isEqualTo(expectedBook))
                .expectComplete()
                .verify();
    }

    @Test
    public void findBookByTitleTest() {
        final Book expectedBook1 = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1).block();
        final Book expectedBook2 = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_3, TEST_GENRE_3).block();
        saveTestBookToDataBase(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        Flux<Book> resultBooks = bookRepository.findBooksByTitle(TEST_TITLE_1);
        StepVerifier
                .create(resultBooks)
                .expectNext(expectedBook1, expectedBook2)
                .expectComplete()
                .verify();
    }

    private Mono<Genre> saveTestGenre(String testName) {
        Genre genre = new Genre();
        genre.setName(testName);
        return genreRepository.save(genre);
    }

    @Test
    public void saveNewBookTest() {
        saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1).block();

        Flux<Book> books = bookRepository.findAll();

        StepVerifier
                .create(books)
                .assertNext(book -> {
                    assertThat(book.getId()).isNotNull();
                    assertThat(book.getTitle()).isEqualTo(TEST_TITLE_1);
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void deleteBookByIdTest() {
        Book book1 = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1).block();
        Book book2 = saveTestBookToDataBase(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2).block();

        Flux<Book> books = bookRepository.findAll();
        StepVerifier
                .create(books)
                .expectNext(book1, book2)
                .expectComplete()
                .verify();

        Long result = bookRepository.deleteBookById(book1.getId()).block();
        assertThat(result > 0).isTrue();

        Flux<Book> books2 = bookRepository.findAll();
        StepVerifier
                .create(books2)
                .assertNext(book -> assertThat(book).isEqualTo(book2))
                .expectComplete()
                .verify();
    }

    @Test
    public void deleteAllTest() throws Exception {
        Book book1 = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1).block();
        Book book2 = saveTestBookToDataBase(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2).block();

        Flux<Book> books = bookRepository.findAll();
        StepVerifier
                .create(books)
                .expectNext(book1, book2)
                .expectComplete()
                .verify();

        bookRepository.deleteAll().block();

        Flux<Book> books2 = bookRepository.findAll();
        StepVerifier
                .create(books2)
                .verifyComplete();
    }
}
