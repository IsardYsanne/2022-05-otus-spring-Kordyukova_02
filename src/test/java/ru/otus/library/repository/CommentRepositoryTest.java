package ru.otus.library.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.library.model.entity.Author;
import ru.otus.library.model.entity.Book;
import ru.otus.library.model.entity.Comment;
import ru.otus.library.model.entity.Genre;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext
@Import({CommentRepositoryImpl.class})
public class CommentRepositoryTest {

    private static final String TEST_TEXT_1 = "testText";

    private static final String TEST_TEXT_2 = "testText2";

    private static final String TEST_USER = "testUser";

    private static final String TEST_TITLE = "testName";

    private static final String TEST_AUTHOR = "testAuthor";

    private static final String TEST_GENRE = "testGenre";

    @Autowired
    private CommentRepositoryImpl commentRepository;

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

    private Genre saveTestGenre(String testName) {
        final Genre genre = new Genre();
        genre.setName(testName);
        return entityManager.persist(genre);
    }

    @Test
    public void saveCommentTest() {
        final Book book = saveTestBookToDataBase(TEST_TITLE, TEST_AUTHOR, TEST_GENRE);
        final Comment comment = new Comment(book, TEST_TEXT_1);

        commentRepository.saveComment(comment);

        Comment testComment = commentRepository.findCommentById(comment.getId());
        assertThat(testComment).isNotNull();
    }

    @Test
    public void deleteCommentByIdWhenSuccessfulTest() {
        final Book book = saveTestBookToDataBase(TEST_TITLE, TEST_AUTHOR, TEST_GENRE);
        final Comment comment = new Comment(book, TEST_TEXT_1);
        commentRepository.saveComment(comment);
        final Long id = comment.getId();

        commentRepository.deleteCommentById(id);

        final Comment testComment = commentRepository.findCommentById(id);
        assertThat(testComment).isNull();
    }

    @Test
    public void deleteCommentByIdWhenNoCommentTest() {
        boolean result = commentRepository.deleteCommentById(1000L);
        assertThat(result).isFalse();
    }
}
