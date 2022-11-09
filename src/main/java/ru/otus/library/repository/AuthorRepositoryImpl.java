package ru.otus.library.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import ru.otus.library.model.entity.Author;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Component
public class AuthorRepositoryImpl implements AuthorRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Author findAuthorById(final String id) {
        return entityManager.find(Author.class, id);
    }

    @Override
    public Author findAuthorByName(final String name) {
        final TypedQuery<Author> query = entityManager.createQuery("SELECT a FROM Author a WHERE a.name = :name", Author.class);
        query.setParameter("name", name);
        try {
            return query.getSingleResult();
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    @Override
    public List<String> findAllAuthorsNames() {
        final TypedQuery<String> query = entityManager.createQuery("SELECT a.name FROM Author a", String.class);
        return query.getResultList();
    }

    @Override
    public Author saveAuthor(final Author author) {
        entityManager.persist(author);
        return author;
    }

    @Override
    public void deleteAuthor(final Author author) {
        entityManager.remove(author);
    }

    @Override
    public boolean deleteAuthorById(final Long id) {
        final Author author = entityManager.find(Author.class, id);
        if (author != null) {
            entityManager.remove(author);
            return true;
        }
        return false;
    }

    @Override
    public int deleteAll() {
        return entityManager.createQuery("DELETE FROM Author").executeUpdate();
    }
}
