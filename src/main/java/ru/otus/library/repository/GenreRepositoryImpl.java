package ru.otus.library.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import ru.otus.library.model.entity.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Component
public class GenreRepositoryImpl implements GenreRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Genre findGenreByName(String name) {
        final TypedQuery<Genre> query = entityManager.createQuery("SELECT g FROM Genre g WHERE g.name = :name", Genre.class);
        query.setParameter("name", name);
        try {
            return query.getSingleResult();
        } catch (DataAccessException e) {
            throw new RuntimeException("Data access error. " + e.getLocalizedMessage());
        }
    }

    @Override
    public List<Genre> findAllGenres() {
        return entityManager.createQuery("SELECT g FROM Genre g", Genre.class).getResultList();
    }

    @Override
    public Genre saveGenre(final Genre genre) {
        entityManager.persist(genre);
        return genre;
    }

    @Override
    public void deleteGenre(final Genre genre) {
        entityManager.remove(genre);
    }

    @Override
    public int deleteAll() {
        return entityManager.createQuery("DELETE FROM Genre").executeUpdate();
    }
}
