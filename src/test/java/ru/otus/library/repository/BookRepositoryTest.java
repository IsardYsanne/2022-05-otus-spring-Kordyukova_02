package ru.otus.library.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.library.model.entity.Author;
import ru.otus.library.model.entity.Book;
import ru.otus.library.model.entity.Comment;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.List;
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

    private static final String TEST_USER_1 = "testUser";

    private static final String TEST_USER_2 = "testUser2";

    private static final String TEST_TEXT_1 = "testText";

    private static final String TEST_TEXT_2 = "testText2";

    @Autowired
    private BookRepository bookRepository;

    private Book saveTestBookToDataBase(String title, String authorName, String genreName) {
        final Set<Author> authors = new HashSet<>();
        Author author = new Author();
        author.setName(authorName);
        authors.add(author);

        final Book book = new Book();
        book.setTitle(title);
        book.setAuthors(authors);
        book.setGenre(genreName);

        return bookRepository.save(book);
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
    public void findBookByIdTest() {
        final Book expectedBook = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        saveTestBookToDataBase(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        final String id = expectedBook.getId();
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

    @Test
    public void saveNewBookTest() {
        final Set<Author> authors = new HashSet<>();
        Author author = new Author();
        author.setName(TEST_AUTHOR_1);
        authors.add(author);

        final Book book = new Book();
        book.setTitle(TEST_TITLE_1);
        book.setGenre(TEST_GENRE_1);
        book.setAuthors(authors);

        bookRepository.save(book);

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

        int result = bookRepository.deleteBookById(book1.getId());

        books = bookRepository.findAll();
        assertThat(result > 0).isTrue();
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

    @Test
    public void getCommentByBookIdTest() {
        final Set<Author> authors = new HashSet<>();
        Author author = new Author();
        author.setName(TEST_AUTHOR_1);
        authors.add(author);

        final Book book = new Book(TEST_TITLE_1, TEST_GENRE_1, authors);
        final Comment comment1 = new Comment(TEST_USER_1, TEST_TEXT_1);
        final Comment comment2 = new Comment(TEST_USER_2, TEST_TEXT_2);
        final Set<Comment> comments = new HashSet<>();
        comments.add(comment1);
        comments.add(comment2);
        book.setComments(comments);
        bookRepository.save(book);

        final Book book1 = bookRepository.findBookWithCommentsById(book.getId());

        assertThat(book1.getComments()).isNotEmpty().hasSize(2);
    }
}
