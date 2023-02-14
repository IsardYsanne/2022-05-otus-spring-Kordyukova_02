package ru.otus.library.service;

import ru.otus.library.model.entity.Genre;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GenreService {

    Flux<Genre> findAllGenres();

    Mono<Genre> saveGenre(Genre genre);

    Mono<Long> deleteGenreByName(String genreName);

    void deleteAll();
}
