package ru.otus.library.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.library.model.entity.Author;
import ru.otus.library.model.entity.Book;
import ru.otus.library.model.entity.Genre;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Iterator;
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
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Book saveTestBookToDataBase(String title, String authorName, String genreName) {
        Author author = new Author();
        author.setName(authorName);
        author = entityManager.persist(author);

        Set<Author> authors = new HashSet<>();
        authors.add(author);

        final Genre genre = saveTestGenre(genreName);

        final Book book = new Book();
        book.setTitle(title);
        book.setAuthors(authors);
        book.setGenre(genre);

        return entityManager.persist(book);
    }

    @Test
    public void findAllBooksTest() {
        List<Book> books = bookRepository.findAll();
        assertThat(books).isEmpty();

        final Book book1 = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        final Book book2 = saveTestBookToDataBase(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        books = bookRepository.findAll();
        assertThat(books)
                .isNotEmpty()
                .hasSize(2)
                .contains(book1, book2);
    }

    @Test
    public void findAllTitlesTest() {
        saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        saveTestBookToDataBase(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        final List<String> titles = bookRepository.findAllTitles();

        assertThat(titles)
                .hasSize(2)
                .contains(TEST_TITLE_1, TEST_TITLE_2);
    }

    @Test
    public void findBookByAuthorIdTest() {
        final Book expectedBook = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        saveTestBookToDataBase(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);
        Iterator<Author> iterator = expectedBook.getAuthors().iterator();
        Author author = iterator.next();

        final List<Book> books = bookRepository.findBooksByAuthorId(author.getId());
        final Book resultBook = books.get(0);

        assertThat(resultBook).isEqualTo(expectedBook);
    }

    @Test
    public void findBookByIdTest() {
        final Book expectedBook = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        saveTestBookToDataBase(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        final Long id = expectedBook.getId();
        final Book resultBook = bookRepository.findById(id).orElseThrow();

        assertThat(resultBook).isEqualTo(expectedBook);
    }

    @Test
    public void findBookByTitleTest() {
        final Book expectedBook1 = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        final Book expectedBook2 = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_3, TEST_GENRE_3);
        saveTestBookToDataBase(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        final List<Book> resultBooks = bookRepository.findBooksByTitle(TEST_TITLE_1);

        assertThat(resultBooks)
                .isNotEmpty()
                .hasSize(2)
                .contains(expectedBook1, expectedBook2);
    }

    private Genre saveTestGenre(String testName) {
        Genre genre = new Genre();
        genre.setName(testName);
        return entityManager.persist(genre);
    }

    @Test
    public void saveNewBookTest() {
        final Book book = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        final List<Book> books = bookRepository.findAll();

        assertThat(books).hasSize(1).contains(book);
    }

    @Test
    public void deleteBookByIdTest() {
        final Book book1 = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        final Book book2 = saveTestBookToDataBase(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        List<Book> books = bookRepository.findAll();
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
        final Book book1 = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        final Book book2 = saveTestBookToDataBase(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        List<Book> books = bookRepository.findAll();
        assertThat(books)
                .isNotEmpty()
                .hasSize(2)
                .contains(book1, book2);

        bookRepository.deleteAll();

        books = bookRepository.findAll();
        assertThat(books).isEmpty();
    }
}
