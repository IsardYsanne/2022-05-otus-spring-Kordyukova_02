package ru.otus.library.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.library.model.entity.Genre;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface GenreRepository extends ReactiveMongoRepository<Genre, String> {

    Flux<Genre> findAll();

    Mono<Genre> findGenreByName(String name);

    Mono<Genre> save(Mono<Genre> genre);

    Mono<Long> deleteGenreByName(String name);
}
