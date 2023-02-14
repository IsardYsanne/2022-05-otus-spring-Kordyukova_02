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
import ru.otus.library.model.entity.Book;
import ru.otus.library.repository.BookRepository;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookServiceTest {

    private static final String TEST_TITLE_1 = "testTitle";

    private static final String TEST_TITLE_2 = "testTitle2";

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private BookServiceImpl bookService;

    @Before
    public void init() {
        reset(bookRepository);
    }

    @Test
    public void findAllBooksTest() {
        Book book = new Book();
        book.setId("id1");
        book.setTitle(TEST_TITLE_1);

        Book book2 = new Book();
        book2.setId("id2");
        book2.setTitle(TEST_TITLE_2);

        Flux<Book> books = Flux.just(book, book2);

        when(bookRepository.findAll()).thenReturn(books);

        Flux<Book> resultList = bookService.findAllBooks();

        verify(bookRepository).findAll();
        StepVerifier
                .create(resultList)
                .expectNextCount(2)
                .expectComplete()
                .verify();
    }
}
