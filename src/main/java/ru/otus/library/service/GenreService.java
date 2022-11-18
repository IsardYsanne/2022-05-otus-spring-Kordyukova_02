package ru.otus.library.service;

import ru.otus.library.model.entity.Genre;

import java.util.List;

public interface GenreService {

    List<String> findAllGenres();

    boolean saveNewGenre(Genre genre);

    boolean deleteGenre(final String genreName);

    void deleteAll();
}
