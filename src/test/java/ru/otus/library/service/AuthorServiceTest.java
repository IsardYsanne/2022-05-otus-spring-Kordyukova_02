package ru.otus.library.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.library.repository.AuthorRepository;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthorServiceTest {

    private static final String TEST_AUTHOR_1 = "testAuthor";

    @MockBean
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorServiceImpl authorService;

    @Before
    public void init() {
        reset(authorRepository);
    }

    @Test
    public void findAllAuthorsNamesTest() {
        authorService.findAllAuthorsNames();
        verify(authorRepository).findAllAuthorsNames();
    }
}
