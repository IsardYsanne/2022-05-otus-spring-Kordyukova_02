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
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.library.dto.AuthorDto;
import ru.otus.library.mapper.AuthorMapper;
import ru.otus.library.service.AuthorService;

@CrossOrigin
@RestController
@RequestMapping("/authors")
@Slf4j
@Api(tags = {"Контроллер для авторов книг"})
public class AuthorController {

    private final AuthorService authorService;

    private final AuthorMapper authorMapper;

    public AuthorController(AuthorService authorService, AuthorMapper authorMapper) {
        this.authorService = authorService;
        this.authorMapper = authorMapper;
    }

    @GetMapping("/show_all")
    public Flux<AuthorDto> showAllAuthors() {
        return authorService.findAllAuthors().map(AuthorMapper::authorToDto);
    }

    @PostMapping("/save")
    public Mono<AuthorDto> addNewAuthor(@RequestBody AuthorDto authorDto) {
        return authorService.saveAuthor(authorMapper.dtoToAuthor(authorDto)).map(AuthorMapper::authorToDto)
                .switchIfEmpty(Mono.error(new RuntimeException()));
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity> deleteAuthor(@PathVariable(name = "id") String id) {
        return authorService.deleteAuthorById(id)
                .map(r -> new ResponseEntity(r == 0? HttpStatus.NOT_FOUND: HttpStatus.OK));
    }
}
