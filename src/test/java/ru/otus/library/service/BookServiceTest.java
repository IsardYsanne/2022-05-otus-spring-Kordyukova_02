package ru.otus.library.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.library.model.entity.Author;
import ru.otus.library.model.entity.Book;
import ru.otus.library.model.entity.Genre;
import ru.otus.library.repository.AuthorRepository;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.repository.GenreRepository;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookServiceTest {

    private static final String TEST_AUTHOR_1 = "testAuthor";

    private static final String TEST_GENRE_1 = "testGenre";

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @Autowired
    private BookServiceImpl bookService;

    @Before
    public void init() {
        reset(bookRepository);
    }

    @Test
    public void findAllBooksTest() {
        bookService.findAllBooks();
        verify(bookRepository).findAll();
    }

    @Test
    public void findBooksByAuthorsNameWhenSuccessfulTest() {
        final Author author = new Author();

        when(authorRepository.findAuthorByName(TEST_AUTHOR_1)).thenReturn(author);
        bookService.findBooksByAuthorsName(TEST_AUTHOR_1);
        verify(bookRepository).findBooksByAuthorId(author.getId());
    }

    @Test
    public void findBooksByAuthorsNameWhenNoAuthorTest() {
        when(authorRepository.findAuthorByName(TEST_AUTHOR_1)).thenReturn(null);
        final List<Book> books = bookService.findBooksByAuthorsName(TEST_AUTHOR_1);
        assertThat(books).isEmpty();
    }

    @Test
    public void saveNewBookTestWhenSuccessful() {
        final List<String> titles = new ArrayList<>();
        when(bookRepository.findAllTitles()).thenReturn(titles);

        final Genre genre = new Genre();
        when(genreRepository.findByName(TEST_GENRE_1)).thenReturn(genre);

        final Author author = new Author();
        when(authorRepository.findAuthorByName(TEST_AUTHOR_1)).thenReturn(author);

        final Book book = new Book();
        book.setId(1L);
        when(bookRepository.save(any())).thenReturn(book);
    }
}
