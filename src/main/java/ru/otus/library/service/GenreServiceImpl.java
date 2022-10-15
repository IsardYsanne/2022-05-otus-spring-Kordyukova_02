package ru.otus.library.service;

import org.springframework.stereotype.Service;
import ru.otus.library.model.entity.Genre;
import ru.otus.library.repository.GenreRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    public GenreServiceImpl(final GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public List<String> getAllGenres() {
        return genreRepository.findAllGenres().stream().map(Genre::getName).collect(Collectors.toList());
    }

    @Override
    public boolean addNewGenre(Genre genre) {
        if (genre.getId() != null) {
            throw new RuntimeException("Такой жанр уже существует.");
        }
        genre = genreRepository.saveGenre(genre);
        return genre.getId() != null;
    }

    @Override
    public boolean deleteGenre(final String genreName) {
        final Genre genre = genreRepository.findGenreByName(genreName);
        if (genre == null) {
            throw new RuntimeException("Такого жанра не существует.");
        }
        int result = genreRepository.deleteGenre(genre);
        return result > 0;
    }

    @Override
    public void deleteAll() {
        genreRepository.deleteAll();
    }
}
