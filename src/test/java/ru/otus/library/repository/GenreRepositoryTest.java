package ru.otus.library.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.library.model.entity.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext
@Import({GenreRepositoryImpl.class})
public class GenreRepositoryTest {

    private static final String TEST_NAME_1 = "testName";

    private static final String TEST_NAME_2 = "testName2";

    @Autowired
    private GenreRepositoryImpl genreRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Genre addTestGenre(String testName) {
        Genre genre = new Genre();
        genre.setName(testName);
        genre = entityManager.persist(genre);
        return genre;
    }

    @Test
    public void addGenreTest() {
        addTestGenre(TEST_NAME_1);
        List<Genre> genres = genreRepository.findAllGenres();
        assertThat(genres).isNotEmpty();
    }

    @Test
    public void getAllGenresTest() {
        List<Genre> genres = genreRepository.findAllGenres();
        assertThat(genres).isEmpty();

        final Genre genre = addTestGenre(TEST_NAME_1);
        final Genre genre2 = addTestGenre(TEST_NAME_2);

        genres = genreRepository.findAllGenres();
        assertThat(genres).hasSize(2).contains(genre, genre2);
    }

    @Test
    public void getGenreByNameTest() {
        final Genre genre1 = addTestGenre(TEST_NAME_1);
        final Genre genre2 = genreRepository.findGenreByName(TEST_NAME_1);
        assertThat(genre2).isEqualTo(genre1);
    }

    @Test
    public void deleteGenreTest() {
        final Genre genre = addTestGenre(TEST_NAME_1);
        final Genre genre2 = addTestGenre(TEST_NAME_2);
        List<Genre> genres = genreRepository.findAllGenres();
        assertThat(genres).hasSize(2).contains(genre, genre2);

        genreRepository.deleteGenre(genre);
        genres = genreRepository.findAllGenres();
        assertThat(genres).hasSize(1).contains(genre2).doesNotContain(genre);
    }

    @Test
    public void deleteAllTest() {
        final Genre genre = addTestGenre(TEST_NAME_1);
        final Genre genre2 = addTestGenre(TEST_NAME_2);
        List<Genre> genres = genreRepository.findAllGenres();
        assertThat(genres).hasSize(2).contains(genre, genre2);

        genreRepository.deleteAll();
        genres = genreRepository.findAllGenres();
        assertThat(genres).isEmpty();
    }
}
