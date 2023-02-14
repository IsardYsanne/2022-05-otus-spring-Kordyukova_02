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

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.List;

@RunWith(SpringRunner.class)
@DataMongoTest
public class AuthorRepositoryTest {

    private static final String TEST_NAME_1 = "testName";

    private static final String TEST_NAME_2 = "testName2";

    @Autowired
    private AuthorRepository authorRepository;

    private Mono<Author> saveTestAuthor(String testName) {
        final Author author = new Author();
        author.setName(testName);
        return authorRepository.save(author);
    }

    @Before
    public void init() {
        authorRepository.deleteAll().block();
    }

    @Test
    public void addNewAuthorTest() {
        final Author author = new Author();
        author.setName(TEST_NAME_1);
        final Mono<Author> authorMono = authorRepository.save(author);
        assertThat(authorMono.block()).isNotNull();

        final Flux<Author> authors = authorRepository.findAll();
        StepVerifier
                .create(authors)
                .assertNext(aut -> {
                    assertThat(aut.getId()).isNotNull();
                    assertThat(aut.getName()).isEqualTo(TEST_NAME_1);
                })
                .thenAwait(Duration.ofSeconds(5))
                .expectComplete()
                .verify();
    }

    @Test
    public void getAllAuthorsTest() {
        final Author author1 = saveTestAuthor(TEST_NAME_1).block();
        final Author author2 = saveTestAuthor(TEST_NAME_2).block();

        final Flux<Author> authorFlux = authorRepository.findAll();
        StepVerifier
                .create(authorFlux)
                .expectNext(author1, author2)
                .thenAwait(Duration.ofSeconds(5))
                .verifyComplete();
    }

    @Test
    public void getAuthorByNameTest() {
        Mono<Author> authorMono = saveTestAuthor(TEST_NAME_1);
        StepVerifier
                .create(authorMono.map(Author::getName))
                .expectNext(TEST_NAME_1).verifyComplete();

        authorMono = authorRepository.findAuthorByName(TEST_NAME_1);
        StepVerifier
                .create(authorMono.map(Author::getName))
                .expectNext(TEST_NAME_1).verifyComplete();
    }

    @Test
    public void deleteAuthorTest() {
        final Author author1 = saveTestAuthor(TEST_NAME_1).block();
        final Author author2 = saveTestAuthor(TEST_NAME_2).block();

        final Flux<Author> authorFlux = authorRepository.findAll();
        StepVerifier
                .create(authorFlux)
                .expectNext(author1, author2)
                .thenAwait(Duration.ofSeconds(5))
                .verifyComplete();

        final Mono<Long> result = authorRepository.deleteAuthorById(author1.getId());
        assertThat(result.block() > 0);

        Flux<Author> authors = authorRepository.findAll();
        StepVerifier
                .create(authors)
                .thenAwait(Duration.ofSeconds(5))
                .assertNext(aut -> {
                    assertThat(aut.getId()).isNotNull();
                    assertThat(aut.getName()).isEqualTo(TEST_NAME_2);
                })
                .thenAwait(Duration.ofSeconds(5))
                .expectComplete()
                .verify();
    }

    @Test
    public void deleteAllTest() {
        final Author author1 = saveTestAuthor(TEST_NAME_1).block();
        final Author author2 = saveTestAuthor(TEST_NAME_2).block();

        final Flux<Author> authorFlux = authorRepository.findAll();
        StepVerifier
                .create(authorFlux)
                .expectNext(author1, author2)
                .verifyComplete();

        authorRepository.deleteAll().block();

        List<Author> authors = authorRepository.findAll().collectList().block();
        assertThat(authors).isEmpty();
    }
}
