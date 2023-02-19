package ru.otus.library.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.library.authentication.CustomUserDetailService;
import ru.otus.library.mapper.BookMapper;
import ru.otus.library.model.entity.Author;
import ru.otus.library.model.entity.Book;
import ru.otus.library.model.entity.Genre;
import ru.otus.library.service.BookService;

import java.util.Arrays;
import java.util.HashSet;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = BookController.class)
@Import(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private BookMapper bookMapper;

    @MockBean
    CustomUserDetailService userDetailService;

    private static final String TEST_TITLE = "testName";

    private static final String TEST_AUTHOR = "testAuthor";

    private static final String TEST_GENRE = "testGenre";

    @Test
    public void showAllBooksTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/show_all")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(bookMapper.bookListToDto((bookService).findAllBooks()));
    }

    @Test
    public void updateBookNoAuthTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .put("/books/update"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void updateBookWithAuthTest() throws Exception {
        when(bookService.findBookById(1L))
                .thenReturn(new Book(
                        TEST_TITLE,
                        new Genre(TEST_GENRE),
                        new HashSet<>(Arrays.asList(new Author(TEST_AUTHOR))), new byte[1]));

        mvc.perform(MockMvcRequestBuilders
                        .put("/books/update"))
                .andExpect(status().isOk());
    }

    @Test
    public void saveBookNoAuthTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .post("/books/save"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void deleteBookNoAuthTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .delete("/books/delete/1"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    public void deleteBookWithWrongAuthTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .delete("/books/delete/1"))
                .andExpect(status().isForbidden());
    }
}
