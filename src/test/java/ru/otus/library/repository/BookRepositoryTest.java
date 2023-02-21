package ru.otus.library.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.library.model.entity.jpa.AuthorJpa;
import ru.otus.library.model.entity.jpa.BookJpa;
import ru.otus.library.model.entity.jpa.GenreJpa;
import ru.otus.library.repository.jpa.BookRepositoryJpa;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext
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
    private BookRepositoryJpa bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    private BookJpa saveTestBookToDataBase(String title, String authorName, String genreName) {
        AuthorJpa author = new AuthorJpa();
        author.setName(authorName);
        author = entityManager.persist(author);
        final Set<AuthorJpa> authors = new HashSet<>();
        authors.add(author);

        final GenreJpa genre = saveTestGenre(genreName);

        final BookJpa book = new BookJpa();
        book.setTitle(title);
        book.setAuthors(authors);
        book.setGenre(genre);

        return entityManager.persist(book);
    }

    private GenreJpa saveTestGenre(String testName) {
        final GenreJpa genre = new GenreJpa();
        genre.setName(testName);
        return entityManager.persist(genre);
    }

    @Test
    public void findAllBooksTest() {
        List<BookJpa> books = bookRepository.findAll();
        assertThat(books).isEmpty();

        final BookJpa book1 = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        final BookJpa book2 = saveTestBookToDataBase(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        books = bookRepository.findAll();
        assertThat(books)
                .isNotEmpty()
                .hasSize(2)
                .contains(book1, book2);
    }

    @Test
    public void findBookByIdTest() {
        final BookJpa expectedBook = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        saveTestBookToDataBase(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        final Long id = expectedBook.getId();
        final BookJpa resultBook = bookRepository.findById(id).orElseThrow();

        assertThat(resultBook).isEqualTo(expectedBook);
    }

    @Test
    public void findBookByTitleTest() {
        final BookJpa expectedBook1 = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        final BookJpa expectedBook2 = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_3, TEST_GENRE_3);
        saveTestBookToDataBase(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        final List<BookJpa> resultBooks = bookRepository.findBooksByTitle(TEST_TITLE_1);

        assertThat(resultBooks)
                .isNotEmpty()
                .hasSize(2)
                .contains(expectedBook1, expectedBook2);
    }

    @Test
    public void saveNewBookTest() {
        AuthorJpa author = new AuthorJpa();
        author.setName(TEST_AUTHOR_1);
        author = entityManager.persist(author);
        final Set<AuthorJpa> authors = new HashSet<>();
        authors.add(author);

        final GenreJpa genre = saveTestGenre(TEST_GENRE_1);

        final BookJpa book = new BookJpa();
        book.setTitle(TEST_TITLE_1);
        book.setAuthors(authors);
        book.setGenre(genre);

        bookRepository.save(book);

        final List<BookJpa> books = bookRepository.findAll();

        assertThat(books)
                .hasSize(1)
                .contains(book);
    }

    @Test
    public void deleteBookByIdTest() {
        final BookJpa book1 = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        final BookJpa book2 = saveTestBookToDataBase(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        List<BookJpa> books = bookRepository.findAll();
        assertThat(books)
                .isNotEmpty()
                .hasSize(2)
                .contains(book1, book2);

        bookRepository.deleteById(book1.getId());

        books = bookRepository.findAll();
        assertThat(books)
                .isNotEmpty()
                .hasSize(1)
                .contains(book2)
                .doesNotContain(book1);
    }

    @Test
    public void deleteAllTest() {
        final BookJpa book1 = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        final BookJpa book2 = saveTestBookToDataBase(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        List<BookJpa> books = bookRepository.findAll();
        assertThat(books)
                .isNotEmpty()
                .hasSize(2)
                .contains(book1, book2);

        bookRepository.deleteAll();

        books = bookRepository.findAll();
        assertThat(books).isEmpty();
    }
}
