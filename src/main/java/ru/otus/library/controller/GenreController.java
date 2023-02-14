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
import ru.otus.library.dto.GenreDto;
import ru.otus.library.mapper.GenreMapper;
import ru.otus.library.service.GenreService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin
@RestController
@RequestMapping("/genres")
@Slf4j
@Api(tags = {"Контроллер для жанров"})
public class GenreController {

    private final GenreService genreService;

    private final GenreMapper genreMapper;

    public GenreController(GenreService genreService, GenreMapper genreMapper) {
        this.genreService = genreService;
        this.genreMapper = genreMapper;
    }

    @GetMapping("/show_all")
    public Flux<GenreDto> showAllGenres() {
        return genreService.findAllGenres().map(GenreMapper::genreToDto);
    }

    @PostMapping("/save")
    public Mono<GenreDto> addNewGenre(@RequestBody GenreDto genreDto) {
        return genreService.saveGenre(genreMapper.dtoToGenre(genreDto)).map(GenreMapper::genreToDto)
                .switchIfEmpty(Mono.error(new RuntimeException()));
    }

    @DeleteMapping("/delete/{name}")
    public Mono<ResponseEntity> deleteGenreByName(@PathVariable(name = "name") String name) {
        return genreService.deleteGenreByName(name)
                .map(r -> new ResponseEntity(r == 0? HttpStatus.NOT_FOUND: HttpStatus.OK));
    }
}
