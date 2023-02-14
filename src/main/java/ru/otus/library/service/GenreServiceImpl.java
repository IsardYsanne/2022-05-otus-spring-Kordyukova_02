package ru.otus.library.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.library.model.entity.Genre;
import ru.otus.library.repository.GenreRepository;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    public GenreServiceImpl(final GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public Flux<Genre> findAllGenres() {
        return genreRepository.findAll();
    }

    @Override
    public Mono<Genre> saveGenre(Genre genre) {
        return genreRepository.findGenreByName(genre.getName()).switchIfEmpty(genreRepository.save(genre));
    }

    @Override
    public Mono<Long> deleteGenreByName(String genreName) {
        return genreRepository.deleteGenreByName(genreName);
    }

    @Override
    public void deleteAll() {
        genreRepository.deleteAll();
    }
}
