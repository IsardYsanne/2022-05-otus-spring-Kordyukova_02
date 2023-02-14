package ru.otus.library.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.library.model.entity.Author;
import ru.otus.library.model.entity.Book;
import ru.otus.library.model.entity.Comment;
import ru.otus.library.model.entity.Genre;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataMongoTest
public class CommentRepositoryTest {

    private static final String TEST_TEXT_1 = "testText";

    private static final String TEST_TEXT_2 = "testText2";

    private static final String TEST_TITLE = "testName";

    private static final String TEST_AUTHOR = "testAuthor";

    private static final String TEST_GENRE = "testGenre";

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Before
    public void init() {
        commentRepository.deleteAll().block();
    }

    private Mono<Book> saveTestBookToDataBase(String title, String authorName, String genreName) {
        Author author = new Author();
        author.setName(authorName);
        author = authorRepository.save(author).block();
        Set<Author> authors = new HashSet<>();
        authors.add(author);

        Genre genre = addTestGenre(genreName).block();

        Book book = new Book();
        book.setTitle(title);
        book.setAuthors(authors);
        book.setGenre(genre);

        return bookRepository.save(book);
    }

    private Mono<Genre> addTestGenre(String testName) {
        Genre genre = new Genre();
        genre.setName(testName);
        return genreRepository.save(genre);
    }

    @Test
    public void findCommentsByBookIdTest() {
        final Book book = saveTestBookToDataBase(TEST_TITLE, TEST_AUTHOR, TEST_GENRE).block();
        Comment comment1 = new Comment(book, TEST_TEXT_1);
        Comment comment2 = new Comment(book, TEST_TEXT_2);

        commentRepository.save(comment1).block();
        commentRepository.save(comment2).block();

        Flux<String> comments = commentRepository.findCommentsByBookId(book.getId()).map(Comment::getCommentText);
        StepVerifier.create(comments)
                .expectNext(TEST_TEXT_1, TEST_TEXT_2)
                .expectComplete()
                .verify();
    }

    @Test
    public void saveCommentTest() {
        Book book = saveTestBookToDataBase(TEST_TITLE, TEST_AUTHOR, TEST_GENRE).block();
        Comment comment = new Comment(book, TEST_TEXT_1);

        Comment comment2 = commentRepository.save(comment).block();

        Mono<Comment> testComment = commentRepository.findById(comment2.getId());
        StepVerifier
                .create(testComment)
                .assertNext(comment1 -> assertThat(comment1).isEqualTo(comment))
                .expectComplete()
                .verify();
    }

    @Test
    public void deleteCommentByIdWhenSuccessfulTest() {
        Book book = saveTestBookToDataBase(TEST_TITLE, TEST_AUTHOR, TEST_GENRE).block();

        Comment comment = new Comment(book, TEST_TEXT_1);
        Comment comment2 = commentRepository.save(comment).block();
        String id = comment2.getId();

        Mono<Long> result = commentRepository.deleteCommentById(id);
        assertThat(result.block() > 0).isTrue();

        Mono<Comment> testComment = commentRepository.findById(comment.getId());
        StepVerifier
                .create(testComment)
                .expectComplete()
                .verify();
    }
}
