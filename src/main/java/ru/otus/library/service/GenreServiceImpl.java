package ru.otus.library.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public List<String> findAllGenres() {
        return genreRepository.findAllGenres().stream().map(Genre::getName).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public boolean saveNewGenre(Genre genre) {
        genre = genreRepository.saveGenre(genre);
        return genre.getId() != null;
    }

    @Transactional
    @Override
    public boolean deleteGenre(final String genreName) {
        final Genre genre = genreRepository.findGenreByName(genreName);
        if (genre == null) {
            throw new RuntimeException("Такого жанра не существует.");
        }
        genreRepository.deleteGenre(genre);
        return true;
    }

    @Override
    public void deleteAll() {
        genreRepository.deleteAll();
    }
}
