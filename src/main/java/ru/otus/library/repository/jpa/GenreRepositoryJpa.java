package ru.otus.library.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.library.model.entity.jpa.GenreJpa;

public interface GenreRepositoryJpa extends JpaRepository<GenreJpa, Long> {

    GenreJpa findByName(final String genreName);
}

