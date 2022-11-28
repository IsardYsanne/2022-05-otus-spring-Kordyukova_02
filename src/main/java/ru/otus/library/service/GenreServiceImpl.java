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
    public Genre findByName(String name) {
        return genreRepository.findByName(name);
    }

    @Override
    public List<String> findAllGenres() {
        return genreRepository.findAll().stream().map(Genre::getName).collect(Collectors.toList());
    }

    @Override
    public boolean saveNewGenre(Genre genre) {
        genre = genreRepository.save(genre);
        return genre.getId() != null;
    }

    @Override
    public void deleteGenre(final String genreName) {
        final Genre genre = genreRepository.findByName(genreName);
        if (genre == null) {
            throw new RuntimeException("Такого жанра не существует.");
        }
        genreRepository.delete(genre);
    }

    @Override
    public void deleteAll() {
        genreRepository.deleteAll();
    }
}
