package ru.otus.library.service;

import ru.otus.library.model.entity.Genre;

import java.util.List;

public interface GenreService {

    Genre findByName(String name);

    List<Genre> findAllGenres();

    Genre saveNewGenre(Genre genre);

    void deleteGenre(final String genreName);

    void deleteGenreById(final Long id);

    void deleteAll();
}
