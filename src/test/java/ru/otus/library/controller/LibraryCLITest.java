package ru.otus.library.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.model.entity.Comment;
import ru.otus.library.repository.AuthorRepositoryImpl;
import ru.otus.library.repository.BookRepositoryImpl;
import ru.otus.library.repository.CommentRepositoryImpl;
import ru.otus.library.repository.GenreRepositoryImpl;
import ru.otus.library.model.entity.Author;
import ru.otus.library.model.entity.Book;
import ru.otus.library.model.entity.Genre;

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
    private BookRepositoryImpl bookRepository;

    @Autowired
    private AuthorRepositoryImpl authorRepository;

    @Autowired
    private GenreRepositoryImpl genreRepository;

    @Autowired
    private CommentRepositoryImpl commentRepository;

    @Before
    public void init() {
        genreRepository.deleteAll();
        authorRepository.deleteAll();
        bookRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    public void findAllAuthorsNamesTest() {
        saveTestAuthor(TEST_AUTHOR_1);
        saveTestAuthor(TEST_AUTHOR_2);

        final Object res = shell.evaluate(() -> "all-authors-names");

        final String result = res.toString();
        final String expected = "[" + TEST_AUTHOR_1 + ", " + TEST_AUTHOR_2 + "]";

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void findAllBooksTest() {
        final Book book1 = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        final Book book2 = saveTestBookToDataBase(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        final Object res = shell.evaluate(() -> "all-books");

        final String result = res.toString();
        final String expected = "Book IDAuthor(s)  Title    Genre     \n";

        assertThat(result).contains(expected,
                String.valueOf(book1.getId()), TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1,
                String.valueOf(book2.getId()), TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);
    }

    @Test
    public void findAllGenresTest() {
        saveTestGenre(TEST_GENRE_1);
        saveTestGenre(TEST_GENRE_2);

        final Object res = shell.evaluate(() -> "all-genres");

        final String result = res.toString();
        final String expected = "[" + TEST_GENRE_1 + ", " + TEST_GENRE_2 + "]";

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void findBooksByAuthorsNameTest() {
        saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        final Book book2 = saveTestBookToDataBase(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        final Object res = shell.evaluate(() -> "book-of-author " + TEST_AUTHOR_2);

        final String result = res.toString();
        final String expected = "Book IDAuthor(s)  Title    Genre     \n";

        assertThat(result).contains(expected,
                String.valueOf(book2.getId()), TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);
    }

    @Test
    public void findAllCommentsForBookTest() {
        Book book = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        Comment comment1 = new Comment(book, TEST_TEXT_1);
        Comment comment2 = new Comment(book, TEST_TEXT_2);

        commentRepository.saveComment(comment1);
        commentRepository.saveComment(comment2);

        Object result = shell.evaluate(() -> "comment-book " + book.getId());

        assertThat(result.toString())
                .isNotNull()
                .contains(TEST_TEXT_1, TEST_TEXT_2);
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
    public void saveNewAuthorTest() {
        shell.evaluate(() -> "add-author " + TEST_AUTHOR_1);

        final Author author = authorRepository.findAuthorByName(TEST_AUTHOR_1);

        assertThat(author).isNotNull();
    }

    @Test
    public void updateBookTitleByIdTest() {
        Book book = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        final Long id = book.getId();

        shell.evaluate(() -> "update-title-id " + id + " " + TEST_TITLE_2);

        book = bookRepository.findBookById(book.getId());
        assertThat(book.getTitle()).isEqualTo(TEST_TITLE_2);
    }

    @Test
    public void updateBookTitleByIdWhenNoBookTest() {
        final Object res = shell.evaluate(() -> "update-title-id " + 100 + " " + TEST_TITLE_2);

        final String result = res.toString();
        final String expected = "Такой книги не существует.";

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void updateCommentByIdTest() {
        Book book = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        Comment comment = new Comment(book, TEST_TEXT_1);
        commentRepository.saveComment(comment);

        shell.evaluate(() -> "update-comment " + comment.getId() + " " + TEST_TEXT_2);

        Comment updatedComment = commentRepository.findCommentById(comment.getId());
        assertThat(updatedComment.getCommentText()).isEqualTo(TEST_TEXT_2);
    }

    private Book saveTestBookToDataBase(String title, String authorName, String genreName) {
        Author author = new Author();
        author.setName(authorName);
        author = authorRepository.saveAuthor(author);
        final Set<Author> authors = new HashSet<>();
        authors.add(author);

        final Genre genre = saveTestGenre(genreName);

        final Book book = new Book();
        book.setTitle(title);
        book.setAuthors(authors);
        book.setGenre(genre);

        return bookRepository.saveBook(book);
    }

    private Author saveTestAuthor(String testName) {
        final Author author = new Author();
        author.setName(testName);
        return authorRepository.saveAuthor(author);
    }

    private Genre saveTestGenre(String testName) {
        Genre genre = new Genre();
        genre.setName(testName);
        genre = genreRepository.saveGenre(genre);
        return genre;
    }

    @Test
    public void deleteBookByIdTest() {
        final Book book1 = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        final Book book2 = saveTestBookToDataBase(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        final Long id = book2.getId();
        shell.evaluate(() -> "delete-book " + id);

        final List<Book> books = bookRepository.findAllBooks();
        assertThat(books)
                .hasSize(1)
                .contains(book1)
                .doesNotContain(book2);
    }

    @Test
    public void deleteBookByIdWhenNoBookTest() {
        final Object res = shell.evaluate(() -> "delete-book " + 100);

        final String result = res.toString();
        final String expected = "Нечего удалять.";

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void deleteAuthorByIdTest() {
        final Author author = saveTestAuthor(TEST_AUTHOR_1);
        saveTestAuthor(TEST_AUTHOR_2);

        shell.evaluate(() -> "delete-author " + author.getId());

        final List<String> authors = authorRepository.findAllAuthorsNames();
        assertThat(authors)
                .hasSize(1)
                .contains(TEST_AUTHOR_2)
                .doesNotContain(TEST_AUTHOR_1);
    }

    @Test
    public void deleteAuthorByIdWhenNoAuthorTest() {
        final Object res = shell.evaluate(() -> "delete-author " + 100);

        final String result = res.toString();
        final String expected = "Нечего удалять.";

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void deleteGenreTest() {
        final Genre genre1 = saveTestGenre(TEST_GENRE_1);
        final Genre genre2 = saveTestGenre(TEST_GENRE_2);

        shell.evaluate(() -> "delete-genre " + TEST_GENRE_1);

        final List<Genre> genres = genreRepository.findAllGenres();
        assertThat(genres)
                .hasSize(1)
                .contains(genre2)
                .doesNotContain(genre1);
    }

    @Test
    public void deleteCommentByIdTest() {
        Book book = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        Comment comment = new Comment(book, TEST_TEXT_1);
        commentRepository.saveComment(comment);
        Long id = comment.getId();

        shell.evaluate(() -> "del-comment " + id);

        Comment testComment = commentRepository.findCommentById(id);
        assertThat(testComment).isNull();
    }
}
