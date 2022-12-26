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
import ru.otus.library.dto.GenreDto;
import ru.otus.library.mapper.GenreMapper;
import ru.otus.library.model.entity.Genre;
import ru.otus.library.service.GenreService;

import java.util.List;

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
    public ResponseEntity<List<GenreDto>> showAllGenres() {
        return ResponseEntity.ok(genreMapper.genreListToDto(genreService.findAllGenres()));
    }

    @PostMapping("/save")
    public ResponseEntity<GenreDto> addNewBook(@RequestBody GenreDto genreDto) {
        final Genre genre = genreService.saveNewGenre(genreMapper.dtoToGenre(genreDto));
        return genre.getId() != null ?
                new ResponseEntity<>(genreMapper.genreToDto(genre), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteBook(@RequestParam(name = "id") Long id) {
        genreService.deleteGenreById(id);
        return ResponseEntity.ok().build();
    }
}
