package ru.otus.library.repository;

import org.springframework.stereotype.Component;
import ru.otus.library.model.entity.Author;
import ru.otus.library.model.entity.Book;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Component
public class BookRepositoryImpl implements BookRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Book findBookById(final Long id) {
        return entityManager.find(Book.class, id);
    }

    @Override
    public List<Book> findAllBooks() {
        return entityManager.createQuery("SELECT b FROM Book b", Book.class).getResultList();
    }

    @Override
    public List<Book> findBooksByAuthor(final Author author) {
        final TypedQuery<Book> query = entityManager.createQuery("SELECT b FROM Book b JOIN b.authors a WHERE a.id = :id", Book.class);
        query.setParameter("id", author.getId());
        return query.getResultList();
    }

    @Override
    public List<Book> findBooksByTitle(final String title) {
        final TypedQuery<Book> query = entityManager.createQuery("SELECT b FROM Book b WHERE b.title = :title", Book.class);
        query.setParameter("title", title);
        return query.getResultList();
    }

    @Override
    public List<String> findAllTitles() {
        return entityManager.createQuery("SELECT b.title FROM Book b", String.class).getResultList();
    }

    @Override
    public Book saveBook(final Book book) {
        entityManager.persist(book);
        return book;
    }

    @Override
    public boolean deleteBookById(Long id) {
        final Book book = entityManager.find(Book.class, id);
        if (book != null) {
            entityManager.remove(book);
            return true;
        }
        return false;
    }

    @Override
    public int deleteAll() {
        return entityManager.createQuery("DELETE FROM Book").executeUpdate();
    }
}
