package ru.otus.library.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.model.entity.jpa.AuthorJpa;
import ru.otus.library.model.entity.jpa.BookJpa;
import ru.otus.library.model.entity.jpa.CommentJpa;
import ru.otus.library.model.entity.jpa.GenreJpa;
import ru.otus.library.repository.jpa.AuthorRepositoryJpa;
import ru.otus.library.repository.jpa.BookRepositoryJpa;
import ru.otus.library.repository.jpa.CommentRepositoryJpa;
import ru.otus.library.repository.jpa.GenreRepositoryJpa;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class LibraryCLITest {

    private static final String TEST_TITLE_1 = "testName";

    private static final String TEST_TITLE_2 = "testName2";

    private static final String TEST_AUTHOR_1 = "testAuthor";

    private static final String TEST_AUTHOR_2 = "testAuthor2";

    private static final String TEST_GENRE_1 = "testGenre";

    private static final String TEST_GENRE_2 = "testGenre2";

    private static final String TEST_TEXT_1 = "testText1";

    private static final String TEST_TEXT_2 = "testText2";

    @Autowired
    private Shell shell;

    @Autowired
    private BookRepositoryJpa bookRepository;

    @Autowired
    private AuthorRepositoryJpa authorRepository;

    @Autowired
    private GenreRepositoryJpa genreRepository;

    @Autowired
    private CommentRepositoryJpa commentRepository;

    @Before
    public void init() {
        genreRepository.deleteAll();
        authorRepository.deleteAll();
        bookRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    public void findAllAuthorsNamesTest() {
        saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        saveTestBookToDataBase(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        final Object res = shell.evaluate(() -> "all-authors-names");

        final String result = res.toString();
        final String expected = "[" + TEST_AUTHOR_1 + ", " + TEST_AUTHOR_2 + "]";

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void findAllBooksTest() {
        final BookJpa book1 = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        final BookJpa book2 = saveTestBookToDataBase(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        final Object res = shell.evaluate(() -> "all-books");

        final String result = res.toString();
        final String expected = "Book IDAuthor(s)  Title    Genre";

        assertThat(result).contains(expected, String.valueOf(book1.getId()), TEST_AUTHOR_1, TEST_TITLE_1, TEST_GENRE_1,
                String.valueOf(book2.getId()), TEST_AUTHOR_2, TEST_TITLE_2, TEST_GENRE_2);
    }

    @Test
    public void findAllGenresTest() {
        saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        saveTestBookToDataBase(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        final Object res = shell.evaluate(() -> "all-genres");

        final String result = res.toString();
        final String expected = "[" + TEST_GENRE_1 + ", " + TEST_GENRE_2 + "]";

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void findBooksByAuthorsNameTest() {
        saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        final BookJpa book2 = saveTestBookToDataBase(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        final Object res = shell.evaluate(() -> "book-of-author " + TEST_AUTHOR_2);

        final String result = res.toString();
        final String expected = "Book IDAuthor(s)  Title    Genre";

        assertThat(result).contains(expected, String.valueOf(book2.getId()), TEST_AUTHOR_2, TEST_TITLE_2, TEST_GENRE_2);
    }

    @Test
    public void findAllCommentsForBookTest() {
        final BookJpa book = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        CommentJpa comment1 = new CommentJpa(book, TEST_TEXT_1);
        CommentJpa comment2 = new CommentJpa(book, TEST_TEXT_2);

        commentRepository.save(comment1);
        commentRepository.save(comment2);

        Object result = shell.evaluate(() -> "comment-book " + book.getId());

        assertThat(result.toString())
                .isNotNull()
                .contains(TEST_TEXT_1, TEST_TEXT_2);
    }

    @Test
    public void saveNewCommentToBookTest() {
        BookJpa book = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        Long id = book.getId();

        shell.evaluate(() -> "add-comment " + id + " " + TEST_TEXT_1);

        List<String> result = commentRepository.findCommentsByBookId(id);
        assertThat(result)
                .isNotNull()
                .hasSize(1)
                .contains(TEST_TEXT_1);
    }

    @Test
    public void saveNewBookOneAuthorWhenAlreadyExistsTest() {
        saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);

        final Object res = shell.evaluate(() -> "add-book " + TEST_TITLE_1 + " " + TEST_GENRE_1 + " " + TEST_AUTHOR_1);

        final String result = res.toString();
        final String expected = "Такая книга уже существует.";

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void updateBookTitleByIdTest() {
        BookJpa book = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        final Long id = book.getId();

        shell.evaluate(() -> "update-title-id " + id + " " + TEST_TITLE_2);

        book = bookRepository.findById(book.getId()).orElseThrow();
        assertThat(book.getTitle()).isEqualTo(TEST_TITLE_2);
    }

    @Test
    public void updateBookTitleByIdWhenNoBookTest() {
        final Object res = shell.evaluate(() -> "update-title-id " + 100 + " " + TEST_TITLE_2);

        final String result = res.toString();
        final String expected = "Такой книги не существует.";

        assertThat(result).isEqualTo(expected);
    }

    private BookJpa saveTestBookToDataBase(String title, String authorName, String genreName) {
        AuthorJpa author = new AuthorJpa();
        author.setName(authorName);
        author = authorRepository.save(author);
        final Set<AuthorJpa> authors = new HashSet<>();
        authors.add(author);

        final GenreJpa genre = saveTestGenre(genreName);

        final BookJpa book = new BookJpa();
        book.setTitle(title);
        book.setAuthors(authors);
        book.setGenre(genre);

        return bookRepository.save(book);
    }

    @Test
    public void deleteBookByIdTest() {
        final BookJpa book1 = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        final BookJpa book2 = saveTestBookToDataBase(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        final Long id = book2.getId();
        shell.evaluate(() -> "delete-book " + id);

        final List<BookJpa> books = bookRepository.findAll();
        assertThat(books)
                .hasSize(1)
                .contains(book1)
                .doesNotContain(book2);
    }

    private GenreJpa saveTestGenre(String testName) {
        GenreJpa genre = new GenreJpa();
        genre.setName(testName);
        genre = genreRepository.save(genre);
        return genre;
    }
}
