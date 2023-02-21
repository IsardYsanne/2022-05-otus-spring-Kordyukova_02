package ru.otus.library.service;

import org.springframework.stereotype.Service;
import ru.otus.library.model.entity.jpa.GenreJpa;
import ru.otus.library.repository.jpa.BookRepositoryJpa;
import ru.otus.library.repository.jpa.GenreRepositoryJpa;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl implements GenreService {

    private BookRepositoryJpa bookRepository;

    private GenreRepositoryJpa genreRepository;

    public GenreServiceImpl(BookRepositoryJpa bookRepository, GenreRepositoryJpa genreRepository) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public GenreJpa findByName(String name) {
        return genreRepository.findByName(name);
    }

    @Override
    public List<String> findAllGenres() {
        return genreRepository.findAll().stream().map(GenreJpa::getName).collect(Collectors.toList());
    }

    @Override
    public boolean saveNewGenre(GenreJpa genre) {
        genre = genreRepository.save(genre);
        return genre.getId() != null;
    }

    @Override
    public void deleteGenre(final String genreName) {
        final GenreJpa genre = genreRepository.findByName(genreName);
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
