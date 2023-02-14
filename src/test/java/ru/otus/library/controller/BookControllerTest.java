package ru.otus.library.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.library.mapper.BookMapper;
import ru.otus.library.model.entity.Book;
import ru.otus.library.service.BookService;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private WebTestClient testClient;

    @MockBean
    private BookService bookService;

    @MockBean
    private BookMapper bookMapper;

    @Test
    public void showAllBooksTest() {
        when(bookService.findAllBooks()).thenReturn(Flux.just(new Book()));

        testClient
                .get()
                .uri("/books/show_all")
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .exchange()
                .expectStatus()
                .isOk();
    }
}
