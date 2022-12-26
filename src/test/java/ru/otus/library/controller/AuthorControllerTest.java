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
import ru.otus.library.mapper.AuthorMapper;
import ru.otus.library.service.AuthorService;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = AuthorController.class)
@Import(AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private AuthorMapper authorMapper;

    @Test
    public void showAllBooksTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/show_all")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(authorMapper.authorListToDto((authorService).findAllAuthors()));
    }

    @Test
    public void saveBookTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/save")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void deleteBookTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/delete")).andExpect(status().isOk());
    }
}
