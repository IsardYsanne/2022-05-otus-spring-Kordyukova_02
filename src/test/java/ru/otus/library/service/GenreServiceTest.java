package ru.otus.library.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.otus.library.model.entity.Genre;
import ru.otus.library.repository.GenreRepository;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GenreServiceTest {

    private static final String TEST_GENRE_1 = "testGenre1";

    private static final String TEST_GENRE_2 = "testGenre2";

    @MockBean
    private GenreRepository genreRepository;

    @Autowired
    private GenreServiceImpl genreService;

    @Before
    public void init() {
        reset(genreRepository);
    }

    @Test
    public void findAllGenresTest() {
        Genre genre = new Genre();
        genre.setId("id1");
        genre.setName(TEST_GENRE_1);

        Genre genre2 = new Genre();
        genre2.setId("id2");
        genre2.setName(TEST_GENRE_2);

        Flux<Genre> genres = Flux.just(genre, genre2);

        when(genreRepository.findAll()).thenReturn(genres);

        Flux<Genre> resultList = genreService.findAllGenres();

        verify(genreRepository).findAll();
        StepVerifier
                .create(resultList)
                .expectNextCount(2)
                .expectComplete()
                .verify();
    }
}
