package ru.otus.library.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.library.model.entity.Genre;
import ru.otus.library.repository.GenreRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;

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
        genre.setId(1L);
        genre.setName(TEST_GENRE_1);

        Genre genre2 = new Genre();
        genre2.setId(2L);
        genre2.setName(TEST_GENRE_2);

        List<Genre> genres = new ArrayList<>(Arrays.asList(genre, genre2));

        when(genreRepository.findAll()).thenReturn(genres);

        List<String> resultList = genreService.findAllGenres();

        verify(genreRepository).findAll();
        assertThat(resultList)
                .isNotEmpty()
                .hasSize(2)
                .contains(TEST_GENRE_1, TEST_GENRE_2);
    }

    @Test
    public void saveNewGenreWhenSuccessfulTest() {
        when(genreRepository.findByName(TEST_GENRE_1)).thenReturn(null);
        Genre genre = new Genre(TEST_GENRE_1);
        genre.setId(1L);
        when(genreRepository.save(any())).thenReturn(genre);
        boolean result = genreService.saveNewGenre(genre);

        assertThat(result).isTrue();
    }
}
