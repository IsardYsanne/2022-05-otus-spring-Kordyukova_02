package ru.otus.library.service;

import java.util.List;

public interface GenreService {

    List<String> findAllGenres();

    void saveGenre(final String genreName, final String bookId);

    void deleteAll();
}
