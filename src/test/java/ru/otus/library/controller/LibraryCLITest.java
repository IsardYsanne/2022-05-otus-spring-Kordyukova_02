package ru.otus.library.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.library.model.entity.Author;
import ru.otus.library.model.entity.Comment;
import ru.otus.library.model.entity.Book;
import ru.otus.library.repository.AuthorRepository;
import ru.otus.library.repository.BookRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LibraryCLITest {

    private static final String TEST_TITLE_1 = "testName";

    private static final String TEST_TITLE_2 = "testName2";

    private static final String TEST_AUTHOR_1 = "testAuthor";

    private static final String TEST_AUTHOR_2 = "testAuthor2";

    private static final String TEST_GENRE_1 = "testGenre";

    private static final String TEST_GENRE_2 = "testGenre2";

    private static final String TEST_TEXT_1 = "testText1";

    private static final String TEST_TEXT_2 = "testText2";

    private static final String TEST_USER_1 = "testUser";

    private static final String TEST_USER_2 = "testUser2";

    @Autowired
    private Shell shell;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Before
    public void init() {
        bookRepository.deleteAll();
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
        final Book book1 = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        final Book book2 = saveTestBookToDataBase(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        final Object res = shell.evaluate(() -> "all-books");

        final String result = res.toString();
        final String expected = "Book ID                 Author(s)  Title    Genre";

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
        final Book book2 = saveTestBookToDataBase(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        final Object res = shell.evaluate(() -> "book-of-author " + TEST_AUTHOR_2);

        final String result = res.toString();
        final String expected = "Book ID                 Author(s)  Title    Genre";

        assertThat(result).contains(expected, String.valueOf(book2.getId()), TEST_AUTHOR_2, TEST_TITLE_2, TEST_GENRE_2);
    }

    @Test
    public void findAllCommentsForBookTest() {
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

        final Object result = shell.evaluate(() -> "comment-book " + book.getId());

        assertThat(result.toString()).isNotNull().contains(TEST_TEXT_1, TEST_TEXT_2);
    }

    @Test
    public void saveNewCommentToBookTest() {
        Book book = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        String id = book.getId();

        shell.evaluate(() -> "add-comment " + id + " " + TEST_USER_1 + " " + TEST_TEXT_1);

        Book result = bookRepository.findBookWithCommentsById(id);
        Comment comment = result.getComments().iterator().next();
        assertThat(comment.getCommentText()).isNotNull().contains(TEST_TEXT_1);
    }

    @Test
    public void saveNewBookOneAuthorWhenAlreadyExistsTest() {
        saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);

        final Object res = shell.evaluate(() -> "add-book " + TEST_TITLE_1 + " " + TEST_GENRE_1 + " " + TEST_AUTHOR_1);

        final String result = res.toString();
        final String expected = "Новая книга была добавлена успешно.";

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void updateBookTitleByIdTest() {
        Book book = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        final String id = book.getId();

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

    private Book saveTestBookToDataBase(String title, String authorName, String genreName) {
        Author author = new Author();
        author.setName(authorName);
        author = authorRepository.save(author);
        final Set<Author> authors = new HashSet<>();
        authors.add(author);

        final Book book = new Book();
        book.setTitle(title);
        book.setAuthors(authors);
        book.setGenre(genreName);

        return bookRepository.save(book);
    }

    @Test
    public void deleteBookByIdTest() {
        final Book book1 = saveTestBookToDataBase(TEST_TITLE_1, TEST_AUTHOR_1, TEST_GENRE_1);
        final Book book2 = saveTestBookToDataBase(TEST_TITLE_2, TEST_AUTHOR_2, TEST_GENRE_2);

        final String id = book2.getId();
        shell.evaluate(() -> "delete-book " + id);

        final List<Book> books = bookRepository.findAll();
        assertThat(books)
                .hasSize(1)
                .contains(book1)
                .doesNotContain(book2);
    }

    @Test
    public void deleteBookByIdWhenNoBookTest() {
        final Object res = shell.evaluate(() -> "delete-book " + 100);

        final String result = res.toString();
        final String expected = "Книга успешно удалена.";

        assertThat(result).isEqualTo(expected);
    }
}
