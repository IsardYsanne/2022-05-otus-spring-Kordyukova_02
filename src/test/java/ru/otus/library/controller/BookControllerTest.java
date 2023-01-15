package ru.otus.library.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.library.dto.BookDto;
import ru.otus.library.mapper.BookMapper;
import ru.otus.library.model.entity.Book;
import ru.otus.library.service.BookService;

import java.util.HashSet;

import static org.mockito.Mockito.verify;
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

    @Test
    public void showAllBooksTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/show_all")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(bookMapper.bookListToDto((bookService).findAllBooks()));
    }

    @Test
    public void saveBookTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/save")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        final BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Title");
        bookDto.setGenre("Genre");
        bookDto.setAuthors("Author");
        bookDto.setComments(new HashSet<>());
        final Book book = bookService.saveNewBook(bookMapper.dtoToBook(bookDto));
        verify(bookMapper.bookToDto(book));
    }

    @Test
    public void deleteBookTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/delete")).andExpect(status().isOk());
    }
}
