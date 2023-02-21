package ru.otus.library.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.library.model.entity.jpa.AuthorJpa;
import ru.otus.library.model.entity.jpa.BookJpa;
import ru.otus.library.model.entity.jpa.GenreJpa;
import ru.otus.library.repository.jpa.AuthorRepositoryJpa;
import ru.otus.library.repository.jpa.BookRepositoryJpa;
import ru.otus.library.repository.jpa.GenreRepositoryJpa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookServiceTest {

    private static final String TEST_AUTHOR_1 = "testAuthor";

    private static final String TEST_GENRE_1 = "testGenre";

    private static final String TEST_GENRE_2 = "testGenre2";

    @MockBean
    private BookRepositoryJpa bookRepository;

    @MockBean
    private AuthorRepositoryJpa authorRepository;

    @MockBean
    private GenreRepositoryJpa genreRepository;

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private AuthorServiceImpl authorService;

    @Autowired
    private GenreServiceImpl genreService;

    @Before
    public void init() {
        reset(bookRepository);
        reset(authorRepository);
        reset(genreRepository);
    }

    @Test
    public void getAllBooksTest() {
        bookService.findAllBooks();
        verify(bookRepository).findAll();
    }

    @Test
    public void getAllAuthorsNamesTest() {
        authorService.findAllAuthorsNames();
        verify(authorRepository).findAllAuthorsNames();
    }

    @Test
    public void getAllGenresTest() {
        final GenreJpa genre = new GenreJpa();
        genre.setId(1L);
        genre.setName(TEST_GENRE_1);

        final GenreJpa genre2 = new GenreJpa();
        genre2.setId(2L);
        genre2.setName(TEST_GENRE_2);

        final List<GenreJpa> genres = new ArrayList<>(Arrays.asList(genre, genre2));

        when(genreRepository.findAll()).thenReturn(genres);

        final List<String> resultList = genreService.findAllGenres();

        verify(genreRepository).findAll();
        assertThat(resultList)
                .isNotEmpty()
                .hasSize(2)
                .contains(TEST_GENRE_1, TEST_GENRE_2);
    }

    @Test
    public void getBooksByAuthorsNameWhenSuccessfulTest() {
        final AuthorJpa author = new AuthorJpa();
        when(authorRepository.findAuthorByName(TEST_AUTHOR_1)).thenReturn(author);

        bookService.findBooksByAuthorsName(TEST_AUTHOR_1);

        verify(bookRepository).findBooksByAuthorId(author.getId());
    }

    @Test
    public void getBooksByAuthorsNameWhenNoAuthorTest() {
        when(authorRepository.findAuthorByName(TEST_AUTHOR_1)).thenReturn(null);

        final List<BookJpa> books = bookService.findBooksByAuthorsName(TEST_AUTHOR_1);

        assertThat(books).isEmpty();
    }

    @Test
    public void addNewGenreWhenSuccessfulTest() {
        when(genreRepository.findByName(TEST_GENRE_1)).thenReturn(null);
        final GenreJpa genre = new GenreJpa(TEST_GENRE_1);
        genre.setId(1L);
        when(genreRepository.save(any())).thenReturn(genre);

        final boolean result = genreService.saveNewGenre(genre);

        assertThat(result).isTrue();
    }

    @Test
    public void addNewBookTestWhenSuccessful() {
        final List<String> titles = new ArrayList<>();
        when(bookRepository.findAllTitles()).thenReturn(titles);

        final GenreJpa genre = new GenreJpa();
        when(genreRepository.findByName(TEST_GENRE_1)).thenReturn(genre);

        final AuthorJpa author = new AuthorJpa();
        when(authorRepository.findAuthorByName(TEST_AUTHOR_1)).thenReturn(author);

        final BookJpa book = new BookJpa();
        book.setId(1L);
        when(bookRepository.save(any())).thenReturn(book);
    }
}
