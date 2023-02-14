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
import ru.otus.library.model.entity.Author;
import ru.otus.library.repository.AuthorRepository;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthorServiceTest {

    private static final String TEST_AUTHOR_1 = "testAuthor";

    private static final String TEST_AUTHOR_2 = "testAuthor2";

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
        Author author = new Author();
        author.setId("id1");
        author.setName(TEST_AUTHOR_1);

        Author author2 = new Author();
        author2.setId("id2");
        author2.setName(TEST_AUTHOR_2);

        Flux<Author> authorFlux = Flux.just(author, author2);

        when(authorRepository.findAll()).thenReturn(authorFlux);

        Flux<Author> resultList = authorService.findAllAuthors();

        verify(authorRepository).findAll();
        StepVerifier
                .create(resultList)
                .expectNextCount(2)
                .expectComplete()
                .verify();
    }
}
