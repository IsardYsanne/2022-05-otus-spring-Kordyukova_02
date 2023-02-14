package ru.otus.library.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.library.dto.BookDto;
import ru.otus.library.mapper.BookMapper;
import ru.otus.library.service.BookService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin
@RestController
@RequestMapping("/books")
@Slf4j
@Api(tags = {"Контроллер для Книг"})
public class BookController {

    private final BookService bookService;

    private final BookMapper bookMapper;

    public BookController(BookService bookService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @GetMapping("/show_all")
    public Flux<BookDto> showAllBooks() {
        return bookService.findAllBooks().map(BookMapper::bookToDto);
    }

    @GetMapping("/show/{id}")
    public Mono<BookDto> showBookForEdit(@PathVariable(name = "id") String id) {
        return bookService.findBookById(id).map(BookMapper::bookToDto);
    }

    @PostMapping("/save")
    public  Mono<BookDto> addNewBook(@RequestBody BookDto bookDto) {
        return bookService.saveBook(bookMapper.dtoToBook(bookDto)).map(BookMapper::bookToDto)
                .switchIfEmpty(Mono.error(new RuntimeException()));
    }

    @PostMapping("/update")
    public Mono<BookDto> updateBook(@RequestBody Mono<BookDto> bookDto) {
        return bookService.updateBook(bookDto.map(BookMapper::dtoToBook)).map(BookMapper::bookToDto);
    }

    @DeleteMapping("/delete")
    public Mono<ResponseEntity> deleteBook(@RequestParam(name = "id") String id) {
        return bookService.deleteBookById(id).map(r -> new ResponseEntity(r == 0 ? HttpStatus.NOT_FOUND : HttpStatus.OK));
    }
}
