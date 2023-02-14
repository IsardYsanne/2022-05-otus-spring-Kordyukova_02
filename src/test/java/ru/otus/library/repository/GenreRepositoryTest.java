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
import ru.otus.library.model.entity.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataMongoTest
public class GenreRepositoryTest {

    private static final String TEST_NAME_1 = "testName";

    private static final String TEST_NAME_2 = "testName2";

    @Autowired
    private GenreRepository genreRepository;

    @Before
    public void init() {
        genreRepository.deleteAll().block();
    }

    private Mono<Genre> addTestGenre(String testName) {
        Genre genre = new Genre();
        genre.setName(testName);
        return genreRepository.save(genre);
    }

    @Test
    public void addGenreTest() {
        addTestGenre(TEST_NAME_1).block();

        Flux<Genre> genres = genreRepository.findAll();

        StepVerifier.create(genres)
                .assertNext(genre -> {
                    assertThat(genre.getId()).isNotNull();
                    assertThat(genre.getName()).isEqualTo(TEST_NAME_1);
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void getAllGenresTest() {
        Genre genre = addTestGenre(TEST_NAME_1).block();
        Genre genre2 = addTestGenre(TEST_NAME_2).block();

        Flux<Genre> genres = genreRepository.findAll();
        StepVerifier
                .create(genres)
                .expectNext(genre, genre2)
                .verifyComplete();
    }

    @Test
    public void getGenreByNameTest() {
        Genre genre1 = addTestGenre(TEST_NAME_1).block();
        Genre genre2 = genreRepository.findGenreByName(TEST_NAME_1).block();
        assertThat(genre2).isEqualTo(genre1);
    }

    @Test
    public void deleteGenreTest() {
        Genre genre = addTestGenre(TEST_NAME_1).block();
        Genre genre2 = addTestGenre(TEST_NAME_2).block();
        Flux<Genre> genres = genreRepository.findAll();
        StepVerifier
                .create(genres)
                .expectNext(genre, genre2)
                .verifyComplete();

        Mono<Long> result = genreRepository.deleteGenreByName(TEST_NAME_1);
        long res = result.block();
        assertThat(res > 0).isTrue();

        genres = genreRepository.findAll();
        StepVerifier.create(genres)
                .assertNext(gen -> {
                    assertThat(gen.getId()).isNotNull();
                    assertThat(gen.getName()).isEqualTo(TEST_NAME_2);
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void deleteAllTest() {
        Genre genre = addTestGenre(TEST_NAME_1).block();
        Genre genre2 = addTestGenre(TEST_NAME_2).block();
        Flux<Genre> genres = genreRepository.findAll();
        StepVerifier
                .create(genres)
                .expectNext(genre, genre2)
                .verifyComplete();

        genreRepository.deleteAll().block();

        Flux<Genre> genres2 = genreRepository.findAll();
        StepVerifier
                .create(genres2)
                .verifyComplete();
    }
}
