package ru.otus.library.repository;

import org.springframework.stereotype.Component;
import ru.otus.library.model.entity.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class CommentRepositoryImpl implements CommentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Comment findCommentById(Long id) {
        return entityManager.find(Comment.class, id);
    }

    @Override
    public Comment saveComment(Comment comment) {
        entityManager.persist(comment);
        return comment;
    }

    @Override
    public boolean deleteCommentById(Long id) {
        final Comment comment = entityManager.find(Comment.class, id);
        if (comment != null) {
            entityManager.remove(comment);
            return true;
        }
        return false;
    }

    @Override
    public void deleteAll() {
        entityManager.createQuery("DELETE FROM Comment").executeUpdate();
    }
}
