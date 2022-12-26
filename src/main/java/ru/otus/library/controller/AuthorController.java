package ru.otus.library.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.library.dto.AuthorDto;
import ru.otus.library.mapper.AuthorMapper;
import ru.otus.library.model.entity.Author;
import ru.otus.library.service.AuthorService;

import java.util.List;

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
    public ResponseEntity<List<AuthorDto>> showAllAuthors() {
        return ResponseEntity.ok(authorMapper.authorListToDto(authorService.findAllAuthors()));
    }

    @PostMapping("/save")
    public ResponseEntity<AuthorDto> addNewBook(@RequestBody AuthorDto authorDto) {
        final Author author = authorService.saveAuthor(authorMapper.dtoToAuthor(authorDto));
        return author.getId() != null ?
                new ResponseEntity<>(authorMapper.authorToDto(author), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteBook(@RequestParam(name = "id") Long id) {
        authorService.deleteAuthorById(id);
        return ResponseEntity.ok().build();
    }
}
